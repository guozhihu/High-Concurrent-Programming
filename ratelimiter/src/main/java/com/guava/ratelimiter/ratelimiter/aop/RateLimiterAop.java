package com.guava.ratelimiter.ratelimiter.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.guava.ratelimiter.ratelimiter.annotation.ExtRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Author: zhihu
 * Description: 使用AOP环绕通知判断拦截所有springmvc请求，判断请求方法上是否存在ExtRateLimiter <br>
 * 1.判断请求方法上是否有@ExtRateLimiter<br>
 * 2.如果方法上存在@ExtRateLimiter注解话<br>
 * 3.使用反射技术获取@ExtRateLimiter注解方法上的参数<br>
 * 4.调用原生RateLimiter代码创建令牌桶<br>
 * 5.如果获取令牌超时的，直接调用服务降级方法（需要自己定义）<br>
 * 6.如果能够获取令牌的话，直接进入实际请求方法。<br>
 * Date: Create in 2019/3/27 17:37
 */
@Aspect
@Component
public class RateLimiterAop {
    
    private Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<String, RateLimiter>();
    
    // 定义切入点 拦截com.guava.ratelimiter.ratelimiter.api
    @Pointcut("execution(public * com.guava.ratelimiter.ratelimiter.api.*.*(..))")
    public void rlAop() {
    }
    
    // 使用AOP环绕通知判断拦截所有springmvc请求，判断请求方法上是否存在ExtRateLimiter注解
    @Around("rlAop()")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 1.如果请求方法上存在@ExtRateLimiter注解的话
        Method sinatureMethod = getSinatureMethod(proceedingJoinPoint);
        if (sinatureMethod == null) {
            // 直接报错
            return null;
        }
        
        // 2.使用java的反射机制获取拦截方法上自定义注解的参数
        ExtRateLimiter extRateLimiter = sinatureMethod.getDeclaredAnnotation(ExtRateLimiter.class);
        if (extRateLimiter == null) {
            // 直接进入实际请求方法中
            return proceedingJoinPoint.proceed();
        }
        
        double permitsPerSecond = extRateLimiter.permitsPerSecond();
        long timeout = extRateLimiter.timeout();
        
        // 3.调用原生的RateLimiter创建令牌 保证每个请求对应都是单例的RateLimiter
        // /index---RateLimiter  /order---RateLimiter   使用hashMap key为请求的url地址
        // 相同的请求在同一个桶
        String requestURI = getRequestURI();
        RateLimiter rateLimiter = null;
        if (rateLimiterMap.containsKey(requestURI)) {
            // 如果在hashMap中对应的requestURI能检测到RateLimiter
            rateLimiter = rateLimiterMap.get(requestURI);
        } else {
            // 如果在hashMap中对应的requestURI没有检测到RateLimiter添加新的RateLimiter
            rateLimiter = RateLimiter.create(permitsPerSecond);
            rateLimiterMap.put(requestURI, rateLimiter);
        }
        
        // 4.获取令牌桶中的令牌，如果没有有效期获取到令牌的话，则直接调用本地服务降级方法，不会进入到实际请求方法中。
        boolean tryAcquire = rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            // 服务降级
            fallback();
            return null;
        }
        
        // 5.获取令牌中的令牌，如果能在有效期获取到令牌的话，则直接进入到实际请求方法中。
        return proceedingJoinPoint.proceed();
    }
    
    private void fallback() throws IOException {
        System.out.println("服务降级别抢了，在抢也是一直等待的，还是放弃吧！！！");
        // 在AOP编程中获取响应
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        // 防止乱码
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            writer.println("别抢了，在抢也是一直等待的，还是放弃吧！！！");
        } finally {
            writer.close();
        }
    }
    
    // 获取客户端请求的URI
    private String getRequestURI() {
        return getRequest().getRequestURI();
    }
    
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }
    
    // 获取到AOP拦截的方法
    private Method getSinatureMethod(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        // 获取到AOP拦截的方法
        Method method = signature.getMethod();
        return method;
    }
}
