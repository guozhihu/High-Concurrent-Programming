package com.disruptor.height.chain;

import com.lmax.disruptor.EventHandler;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/18 9:40
 */
public class Handler4 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("handler4 : SET PRICE");
        Thread.sleep(1000);
        event.setPrice(17.0);
    }
}
