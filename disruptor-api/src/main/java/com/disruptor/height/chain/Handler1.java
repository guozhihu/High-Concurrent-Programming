package com.disruptor.height.chain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/18 9:40
 */
public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade> {
    
    // EventHandler
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }
    
    // WorkHandler
    @Override
    public void onEvent(Trade event) throws Exception {
        System.err.println("handler1 : SET NAME");
        Thread.sleep(1000); // 用于模拟耗时
        event.setName("H1");
    }
}
