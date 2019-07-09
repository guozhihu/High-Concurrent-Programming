package com.disruptor.netty;

import com.disruptor.netty.client.MessageConsumerImpl4Client;
import com.disruptor.netty.client.NettyClient;
import com.disruptor.netty.disruptor.MessageConsumer;
import com.disruptor.netty.disruptor.RingBufferWorkerPoolFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyClientApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DisruptorNettyClientApplication.class, args);
    
        MessageConsumer[] consumers = new MessageConsumer[4];
        for (int i = 0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new MessageConsumerImpl4Client("code:clientId:" + i);
            consumers[i] = messageConsumer;
        }
    
        RingBufferWorkerPoolFactory.getInstance().initAndStart(
            ProducerType.MULTI,
            1024 * 1024,
            new BlockingWaitStrategy(),
            consumers
        );
        
        // 建立连接 并发送消息
        new NettyClient().sendData();
    }
}
