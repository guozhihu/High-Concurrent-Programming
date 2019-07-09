package com.disruptor.netty;

import com.disruptor.netty.disruptor.MessageConsumer;
import com.disruptor.netty.disruptor.RingBufferWorkerPoolFactory;
import com.disruptor.netty.server.MessageConsumerImpl4Server;
import com.disruptor.netty.server.NettyServer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DisruptorNettyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisruptorNettyServerApplication.class, args);
    
        MessageConsumer[] consumers = new MessageConsumer[4];
        for (int i = 0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new MessageConsumerImpl4Server("code:serverId:" + i);
            consumers[i] = messageConsumer;
        }
        RingBufferWorkerPoolFactory.getInstance().initAndStart(
            ProducerType.MULTI,
            1024 * 1024,
            new BlockingWaitStrategy(),
            consumers
        );
        
        new NettyServer().bind(8765);
    }

}
