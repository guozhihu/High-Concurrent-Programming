package com.guava.ratelimiter.ratelimiter.service;

import org.springframework.stereotype.Service;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/3/27 17:32
 */
@Service
public class OrderService {
    
    public boolean addOrder() {
        System.out.println("db...正在操作订单表数据库...");
        return true;
    }
}
