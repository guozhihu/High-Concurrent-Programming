package com.disruptor.height.chain;

import com.lmax.disruptor.EventHandler;

import java.util.UUID;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/18 9:40
 */
public class Handler2 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("handler2 : SET ID");
        Thread.sleep(2000); // 用于模拟耗时
        event.setId(UUID.randomUUID().toString());
    }
}
