package com.wf.ew.plugin.log.aspect;

import com.wf.ew.common.utils.UserAgentGetter;
import com.wf.ew.plugin.log.ILogService;
import com.wf.ew.plugin.log.annotation.Log;
import com.wf.ew.plugin.log.annotation.LogSubject;
import com.wf.ew.system.model.User;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private ILogService logService;
    @Autowired
    private HttpServletRequest request;

    @Around("within(com.*..*) && @annotation(archivesLog)")
    public Object around(ProceedingJoinPoint pjd, Log archivesLog) throws Throwable {

        //执行方法，获取返回参数
        Object result  = null;

        com.wf.ew.plugin.log.Log log = new com.wf.ew.plugin.log.Log();

        log.setLogId(null);
        log.setCtime(LocalDateTime.now());
        long startTime=System.currentTimeMillis();

        //类名
        Class<?> classTarget = pjd.getTarget().getClass();
        String className = classTarget.getName();
        log.setClasz(className);

        // 获取当前方法
        Signature signature = pjd.getSignature();
        MethodSignature methodSignature = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        methodSignature = (MethodSignature) signature;
        Method currentMethod = classTarget.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        Log currentLog =  currentMethod.getAnnotation(Log.class);
        String message = currentLog.value();

        // 判断是否需要添加日志
        LogSubject logSubject = classTarget.getAnnotation(LogSubject.class);
        if (logSubject != null) {
            if (logSubject.closed()) {
                return pjd.proceed();
            }
            message = message + logSubject.value();
        }
        log.setMessage(message);

        //方法名
        String methodName = pjd.getSignature().getName();
        log.setMethod(methodName);

        //获取请求参数
        Object[] params = pjd.getArgs();
        String paramsStr = "";
        for(Object param:params) {
            paramsStr += (param + ",");
        }
        log.setParams(paramsStr);

        //请求地址
        String uri = request.getRequestURI();
        log.setUri(uri);

        //IP
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        String host = agentGetter.getIpAddr();
        log.setHost(host);
        log.setBrowserType(agentGetter.getBrowser());
        log.setDevice(agentGetter.getDevice());
        log.setOsName(agentGetter.getOS());

        //操作人
        User thisUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (thisUser != null){
            int userId = thisUser.getUserId();
            log.setUserId(userId);
        }

        //结束时间
        long endTime=System.currentTimeMillis();
        float excTime=(float)(endTime-startTime) / 1000;
        log.setEndtime(LocalDateTime.now());
        log.setExctime(excTime);

        try {
            result = pjd.proceed();
        } catch (Exception e) {
            log.setThrowables(e.getMessage());
        }

        //录入数据库
        logService.save(log);

        return result;
    }


}