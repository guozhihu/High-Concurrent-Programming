package com.disruptor.netty.client;

import com.disruptor.netty.disruptor.MessageProducer;
import com.disruptor.netty.disruptor.RingBufferWorkerPoolFactory;
import com.disruptor.netty.entity.TranslatorData;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/23 12:11
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TranslatorData response = (TranslatorData) msg;
        // 不要再这里编写任何有关接收到的数据处理的业务逻辑，可以交给一个线程池去异步的调用执行
        String producerId = "code:sessionId:002";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        // 数据msg是从通道中的缓冲区接收的，处理完后要清理掉
        messageProducer.onData(response, ctx);
    }
}
