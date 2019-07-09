package com.disruptor.netty.server;

import com.disruptor.netty.disruptor.MessageProducer;
import com.disruptor.netty.disruptor.RingBufferWorkerPoolFactory;
import com.disruptor.netty.entity.TranslatorData;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Author: zhihu
 * Description: 服务器端的网络事件处理器
 * Date: Create in 2019/4/23 10:54
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TranslatorData request = (TranslatorData) msg;
//        System.err.println("Server端：id = " + request.getId()
//            + ", name = " + request.getName()
//            + ", message = " + request.getMessage());
//        // 不要在这里处理业务逻辑，如数据库持久化操作IO读写 ---> 交给一个线程池去异步的调用执行
//        TranslatorData response = new TranslatorData();
//        response.setId("resp: " + request.getId());
//        response.setName("resp: " + request.getName());
//        response.setMessage("resp: " + request.getMessage());
//        // 写出response响应消息
//        ctx.writeAndFlush(response);
        // 自己的应用服务应该有一个ID生成规则
        String producerId = "code:sessionId:001";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.onData(request, ctx);
    }
}
