package com.wf.ew.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.exception.BusinessException;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.system.dao.UserRoleMapper;
import com.wf.ew.system.model.Role;
import com.wf.ew.system.model.User;
import com.wf.ew.system.dao.UserMapper;
import com.wf.ew.system.model.UserRole;
import com.wf.ew.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户表 服务实现类
 *
 * @author wangfan
 * @since 2019-02-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public User getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public PageResult<User> listUser(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null || limit == null) {
            page = 1;
            limit = 10;
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtil.isNotBlank(searchValue) && StringUtil.isNotBlank(searchKey)) {
            wrapper.eq(searchKey, searchValue);
        }
        IPage<User> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        // 查询user的角色
        List<User> userList = iPage.getRecords();
        if (userList != null && userList.size() > 0) {
            List<UserRole> userRoles = userRoleMapper.selectByUserIds(getUserIds(userList));
            for (User one : userList) {
                List<Role> tempURs = new ArrayList<>();
                for (UserRole ur : userRoles) {
                    if (one.getUserId().equals(ur.getUserId())) {
                        Role r = new Role();
                        r.setRoleId(ur.getRoleId());
                        r.setRoleName(ur.getRoleName());
                        tempURs.add(r);
                    }
                }
                one.setRoles(tempURs);
            }
        }
        return new PageResult<>(userList, iPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(User user, List<Integer> roleIds) {
        if (baseMapper.selectByUsername(user.getUsername()) != null) {
            throw new BusinessException("账号已经存在");
        }
        boolean rs = baseMapper.insert(user) > 0;
        if (rs) {
            if (userRoleMapper.insertBatch(user.getUserId(), roleIds) < roleIds.size()) {
                throw new BusinessException("添加失败，请重试");
            }
        }
        return rs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(User user, List<Integer> roleIds) {
        user.setUsername(null);  // 账号不能修改
        boolean rs = baseMapper.updateById(user) > 0;
        if (rs) {
            userRoleMapper.delete(new UpdateWrapper<UserRole>().eq("user_id", user.getUserId()));
            if (userRoleMapper.insertBatch(user.getUserId(), roleIds) < roleIds.size()) {
                throw new BusinessException("修改失败，请重试");
            }
        }
        return rs;
    }

    // 获取用户id
    private List<Integer> getUserIds(List<User> userList) {
        List<Integer> userIds = new ArrayList<>();
        for (User one : userList) {
            userIds.add(one.getUserId());
        }
        return userIds;
    }

}
