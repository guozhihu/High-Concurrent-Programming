package com.disruptor.disruptor_02_quickstart;


import com.lmax.disruptor.EventHandler;

/**
 * Author: zhihu
 * Description: 监听事件类，用于处理数据（Event类）
 * Date: Create in 2019/3/10 14:16
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {
    
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        // Thread.sleep(Integer.MAX_VALUE);
        System.err.println("消费者：" + event.getValue());
    }
}
