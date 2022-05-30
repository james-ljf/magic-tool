package com.magictool.web.aspectj;

import com.magictool.web.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * @Author: ljf
 * @DateTime: 2022/5/30 21:48
 */
@Slf4j
@Aspect
@Component
public class SystemTimeAspectj {

    @Pointcut("@annotation(对应注解SystemTime的包路径) || @within(对应注解的包路径)")
    public void currentTimeLog() {
        // TODO
    }


    @Around("currentTimeLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //开始时间
        long start = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        long end = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取原方法的参数
        Object[] args = point.getArgs();
        //请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        String request = Arrays.toString(args);
        log.debug("【接口执行时间】接口名：{}.{}, 请求信息：{}\n 执行时间:{}毫秒",className, methodName, request, (end-start));
        log.info("【接口执行时间】接口名：{}.{}, 请求信息：{}\n执行时间:{}毫秒",className, methodName, request, (end-start));
        return result;
    }

}
