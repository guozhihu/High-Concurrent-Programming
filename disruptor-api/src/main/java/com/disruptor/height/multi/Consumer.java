package com.disruptor.height.multi;

import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/19 9:15
 */
public class Consumer implements WorkHandler<Order> {
    
    private String consumerId;
    
    private static AtomicInteger count = new AtomicInteger(0);
    
    private Random random = new Random();
    
    private CountDownLatch consumeEnd;
    
    public Consumer(String consumerId, CountDownLatch consumeEnd) {
        this.consumerId = consumerId;
        this.consumeEnd = consumeEnd;
    }
    
    
    @Override
    public void onEvent(Order event) throws Exception {
        Thread.sleep(1 * random.nextInt(2));
        System.err.println("当前消费者：" + this.consumerId + ", 消费信息ID：" + event.getId());
        count.incrementAndGet();
        consumeEnd.countDown();
        // System.out.println("end: " + consumeEnd.getCount());
    }
    
    public int getCount() {
        return count.get();
    }
}
