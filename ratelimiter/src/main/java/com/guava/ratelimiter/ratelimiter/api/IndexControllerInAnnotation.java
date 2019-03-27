package com.guava.ratelimiter.ratelimiter.api;

import com.guava.ratelimiter.ratelimiter.annotation.ExtRateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: zhihu
 * Description: 使用RateLimiter 实现令牌通方式限流  利用aop实现自定义ratelimiter限流注解框架（推荐该方式）
 * Date: Create in 2019/3/27 19:05
 */
@RestController
public class IndexControllerInAnnotation {
    
    // 使用注解方式实现服务令牌桶限流，permitsPerSecond=0.5等同于每两秒钟生成一个令牌，timeout表示获取令牌的超时时间，为500毫秒
    @RequestMapping("/findOrder")
    @ExtRateLimiter(permitsPerSecond = 0.5, timeout = 500)
    public String findOrder() {
        System.out.println("findOrder");
        return "SUCESS";
    }
    
    // 使用注解方式实现服务令牌桶限流，permitsPerSecond=10.0等同于每秒钟生成10个令牌，timeout表示获取令牌的超时时间，为500毫秒
    @RequestMapping("/myOrder")
    @ExtRateLimiter(permitsPerSecond = 10.0, timeout = 500)
    public String myOrder() {
        System.out.println("myOrder");
        return "SUCCESS";
    }
    
    // 没有实现限流
    @RequestMapping("/orderIndex")
    public String orderIndex() throws InterruptedException {
        System.out.println("orderIndex");
        return "orderIndex_success";
    }
}
