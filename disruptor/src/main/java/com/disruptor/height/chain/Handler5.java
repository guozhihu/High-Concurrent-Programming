package com.disruptor.height.chain;


import com.lmax.disruptor.EventHandler;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/18 9:40
 */
public class Handler5 implements EventHandler<Trade> {
    
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("handler5 : GET PRICE: " + event.getPrice());
        Thread.sleep(1000);
        event.setPrice(event.getPrice() + 3.0);
    }
}
