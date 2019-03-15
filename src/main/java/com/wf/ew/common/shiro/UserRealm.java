package com.wf.ew.common.shiro;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.ew.common.utils.RedisUtil;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.system.model.Authorities;
import com.wf.ew.system.model.User;
import com.wf.ew.system.model.UserRole;
import com.wf.ew.system.service.AuthoritiesService;
import com.wf.ew.system.service.UserRoleService;
import com.wf.ew.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionContext;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.*;

/**
 * Shiro认证和授权
 * Created by wangfan on 2018-02-22 上午 11:29
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 角色
        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", user.getUserId()));
        Set<String> roles = new HashSet<>();
        for (int i = 0; i < userRoles.size(); i++) {
            roles.add(String.valueOf(userRoles.get(i).getRoleId()));
        }
        authorizationInfo.setRoles(roles);
        // 权限
        List<Authorities> authorities = authoritiesService.listByUserId(user.getUserId());
        Set<String> permissions = new HashSet<>();
        for (int i = 0; i < authorities.size(); i++) {
            String authority = authorities.get(i).getAuthority();
            if (StringUtil.isNotBlank(authority)) {
                permissions.add(authority);
            }
        }
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UnknownAccountException(); // 账号不存在
        }
        if (user.getState() != 0) {
            throw new LockedAccountException();  // 账号被锁定
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(EndecryptUtil.DEFAULT_SALT), getName());

        excludeUser(user.getUserId());

        return authenticationInfo;
    }

    /**
     * 排除同一账号的已登录session
     * @param user
     */
    private void excludeUser(int user){
        // 获取所有的sessionId
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        Iterator<Session> it = sessions.iterator();
        while (it.hasNext()) {
            Session session = it.next();
            SimplePrincipalCollection principalCollection = (SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (principalCollection != null) {
                User sessionUser = (User)principalCollection.getPrimaryPrincipal();
                // 相同的id说明已登录
                if (sessionUser != null && sessionUser.getUserId() == user){
                    // 强制下线前一个会话
                    sessionDAO.delete(session);
                    return;
                }
            }
        }
    }

    /**
     * 通过Redis实现session共享
     * @param user
     */
    private void shareSession(String user){
        String sessionKey = "session:user:" + user;
        String sessionValue = redisUtil.get(sessionKey);
        String sessionId = StrUtil.toString(SecurityUtils.getSubject().getSession().getId());
        if (StrUtil.isNotBlank(sessionValue) && !StrUtil.equals(sessionValue, sessionId)) {
            redisUtil.delete(sessionKey);
        }
        redisUtil.set(sessionKey, sessionId);
    }
}
