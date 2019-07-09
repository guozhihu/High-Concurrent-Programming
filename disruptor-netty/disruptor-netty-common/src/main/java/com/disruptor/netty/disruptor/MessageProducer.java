package com.disruptor.netty.disruptor;

import com.disruptor.netty.entity.TranslatorData;
import com.disruptor.netty.entity.TranslatorDataWapper;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/23 10:17
 */
public class MessageProducer {
    
    private String producerId;
    
    private RingBuffer<TranslatorDataWapper> ringBuffer;
    
    public MessageProducer(String producerId, RingBuffer<TranslatorDataWapper> ringBuffer){
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }
    
    public void onData(TranslatorData data, ChannelHandlerContext ctx) {
        long sequence = ringBuffer.next();
        try {
            TranslatorDataWapper wapper = ringBuffer.get(sequence);
            wapper.setData(data);
            wapper.setCtx(ctx);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
