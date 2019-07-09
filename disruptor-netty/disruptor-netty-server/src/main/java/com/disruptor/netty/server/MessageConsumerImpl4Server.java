package com.disruptor.netty.server;

import com.disruptor.netty.disruptor.MessageConsumer;
import com.disruptor.netty.entity.TranslatorData;
import com.disruptor.netty.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/23 11:05
 */
public class MessageConsumerImpl4Server extends MessageConsumer {
    
    public MessageConsumerImpl4Server(String consumerId) {
        super(consumerId);
    }
    
    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        TranslatorData request = event.getData();
        ChannelHandlerContext ctx = event.getCtx();
        // 1.业务处理逻辑
        System.out.println("Server端：id = " + request.getId()
            + ", name = " + request.getName()
            + ", message = " + request.getMessage());
        
        // 2.回送响应消息
        TranslatorData response = new TranslatorData();
        response.setId("resp: " + request.getId());
        response.setName("resp: " + request.getName());
        response.setMessage("resp: " + request.getMessage());
        // 写出response响应消息
        ctx.writeAndFlush(response);
    }
}
