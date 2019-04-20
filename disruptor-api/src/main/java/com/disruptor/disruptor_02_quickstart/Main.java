package com.disruptor.disruptor_02_quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/3/10 14:24
 */
public class Main {
    
    public static void main(String[] args) {
        // 创建工厂
        OrderEventFactory orderEventFactory = new OrderEventFactory();
        // 创建ringBuffer大小
        int ringBufferSize = 4; // ringBufferSize大小一定要是2的N次方
        // 创建一个可缓存的线程 提供线程来触发Consumer 的事件处理
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        /**
         * 1 eventFactory: 消息(event)工厂对象
         * 2 ringBufferSize: 容器的长度
         * 3 executor: 线程池(建议使用自定义线程池) RejectedExecutionHandler
         * 4 ProducerType: 单生产者 还是 多生产者
         * 5 waitStrategy: 等待策略
         */
        // 1.实例化disruptor对象
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(
            orderEventFactory,
            ringBufferSize,
            executor,
            ProducerType.SINGLE,
            new BlockingWaitStrategy()
        );
        
        // 2.添加消费者的监听（构建disruptor与消费者的一个关联关系）
        disruptor.handleEventsWith(new OrderEventHandler());
        
        // 3.启动disruptor
        disruptor.start();
        
        // 4.获取实际存储数据的容器：RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        
        // 5.创建生产者
        OrderEventProducer producer = new OrderEventProducer(ringBuffer);
        
        // 6.指定缓冲区大小
        ByteBuffer bb = ByteBuffer.allocate(8);
        
        for (long i = 0; i < 5; i++) {
            bb.putLong(0, i);
            producer.sendData(bb);
        }
        
        // 7.关闭disruptor和executor
        disruptor.shutdown();
        executor.shutdown();
    }
}
