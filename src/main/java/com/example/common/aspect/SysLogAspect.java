package com.example.common.aspect;

import com.alibaba.fastjson.JSON;
import com.example.common.annotation.SysLog;
import com.example.common.utils.HttpContextUtils;
import com.example.common.utils.MyIPUtil;
import com.example.modules.sys.dao.mongo.SysLogDAO;
import com.example.modules.sys.domain.SysLogDO;
import com.example.modules.sys.domain.SysUserDO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志，切面处理类
 *
 * @author dj
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Resource
    private SysLogDAO sysLogDAO;

    @Pointcut("@annotation(com.example.common.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogDO sysLog = new SysLogDO();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSON.toJSONString(args[0]);
            sysLog.setParams(params);
        } catch (Exception e) {
            log.warn("参数获取错误");
        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(MyIPUtil.getIpAddr(request));

        //用户名
        SysUserDO user = null;
        if (null != user) {
            sysLog.setUsername(user.getUsername());
            sysLog.setUserId(user.getUserId() + "");
        }
        sysLog.setRunTime(time);
        sysLog.setCreateDate(new Date());
        //保存系统日志
        sysLogDAO.insert(sysLog);
    }

}
