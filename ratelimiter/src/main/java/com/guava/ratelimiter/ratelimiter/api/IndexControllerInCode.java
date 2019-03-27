package com.guava.ratelimiter.ratelimiter.api;

import com.google.common.util.concurrent.RateLimiter;
import com.guava.ratelimiter.ratelimiter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Author: zhihu
 * Description: 使用RateLimiter 实现令牌通方式限流  使用原生代码实现（非常不推荐该方式）
 * Date: Create in 2019/3/27 18:30
 */
@RestController
public class IndexControllerInCode {
    
    @Autowired
    private OrderService orderService;
    
    // create方法中传入一个参数  以每秒为单位固定的速率值1r/s  每秒钟往桶中存入1个令牌
    RateLimiter rateLimiter = RateLimiter.create(1); // 独立线程
    
    // 相当于该接口每秒钟时间 只能支持1个客户端访问
    @RequestMapping("/addOrder")
    public String addOrder() {
        // 1.限流处理 限流正常要放在网关 客户端从桶中获取对应的令牌，为什么返回double类型的结果，这个结果表示从桶中拿到令牌等待的时间。
        // 2.如果获取不到令牌，就会一直等待。
        // double acquire = rateLimiter.acquire();
        // System.out.println("从桶中获取令牌等待的时间：" + acquire);
        
        // 下面这种方式设置服务降级处理（相当于配置在规定时间内如果没有获取到令牌的话，直接走服务降级。）
        // 如果在500毫秒内如果没有获取到令牌的话，则直接走服务降级处理
        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            System.out.println("别抢了， 在抢也是一直等待的， 还是放弃吧！！！");
            return "别抢了， 在抢也是一直等待的， 还是放弃吧！！！";
        }
        
        // 2.业务逻辑处理
        boolean addOrderResult = orderService.addOrder();
        if (addOrderResult) {
            System.out.println("恭喜您，抢购成功! 等待时间:" + rateLimiter.acquire());
            return "恭喜您，抢购成功!";
        }
        
        return "抢购失败";
    }
}
