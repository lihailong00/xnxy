package com.lee.xnxydev.aop;

import com.alibaba.fastjson2.JSON;
import com.lee.xnxydev.util.HttpContextUtil;
import com.lee.xnxydev.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 关于@Aspect注解：
 *      切面：定义了切点和通知的关系
 * @author 晓龙coding
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.lee.xnxydev.aop.LogAnnotation)")
    public void pointCut() {

    }

    /**
     * 环绕通知，可以对方法前后进行增强
     * @return
     */
    @Around("pointCut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = joinPoint.proceed();
        Long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        recordLog(joinPoint, time);
        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, Long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("======log start======");
        log.info("module:{}", logAnnotation.module());
        log.info("operator:{}", logAnnotation.operator());
        log.info("当前时间:{}", LocalDateTime.now());
        log.info("执行时间:{}ms", time);
        if (time >= 5000) {
            log.info("重要漏洞，执行时间:{}ms", time);
        }
        else if (time >= 2000) {
            log.info("一般漏洞，执行时间:{}ms", time);
        }
        else if (time >= 1000) {
            log.info("轻微漏洞，执行时间:{}ms", time);
        }
        // 请求方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("请求方法名:{}", className + "." + methodName + "()");
        // 请求参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}", params);
        // 获取http请求信息和ip地址
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ipAddress = IPUtil.getIpAddr(request);
        log.info("IP地址：{}", ipAddress);
        log.info("======log end======");
    }
}
