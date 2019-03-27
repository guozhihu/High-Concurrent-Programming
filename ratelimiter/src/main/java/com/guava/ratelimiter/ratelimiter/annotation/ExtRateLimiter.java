package com.guava.ratelimiter.ratelimiter.annotation;

import java.lang.annotation.*;

/**
 * Author: zhihu
 * Description: 自定义服务限流注解框架 原理：参考：RateLimiter
 * Date: Create in 2019/3/27 17:34
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtRateLimiter {
    
    // 以每秒为单位固定的速率值往令牌桶中添加令牌
    double permitsPerSecond();
    
    // 在规定的毫秒数中,如果没有获取到令牌的话，则直接走服务降级处理
    long timeout();
}
