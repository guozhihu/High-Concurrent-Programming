package com.disruptor.netty.client;

import com.disruptor.netty.disruptor.MessageConsumer;
import com.disruptor.netty.entity.TranslatorData;
import com.disruptor.netty.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/23 11:45
 */
public class MessageConsumerImpl4Client extends MessageConsumer {
    
    public MessageConsumerImpl4Client(String consumerId) {
        super(consumerId);
    }
    
    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        TranslatorData response = event.getData();
        ChannelHandlerContext ctx = event.getCtx();
        // 业务逻辑处理
        try {
            System.out.println("Client端：id = " + response.getId()
                + ", name = " + response.getName()
                + ", message = " + response.getMessage());
        } finally {
            ReferenceCountUtil.release(response);
        }
    }
}
