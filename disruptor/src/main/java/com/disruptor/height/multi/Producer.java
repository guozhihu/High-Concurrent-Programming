package com.disruptor.height.multi;

import com.lmax.disruptor.RingBuffer;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/19 9:01
 */
public class Producer {
    
    private RingBuffer<Order> ringBuffer;
    
    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
    
    public void sendData(String uuid) {
        long sequence = ringBuffer.next();
        try {
            Order order = ringBuffer.get(sequence);
            order.setId(uuid);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
