package com.disruptor.height.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: zhihu
 * Description:
 * Date: Create in 2019/4/19 8:57
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        
        ExecutorService es = Executors.newFixedThreadPool(5);
        
        // 1.创建RingBuffer
        RingBuffer ringBuffer = RingBuffer.create(ProducerType.MULTI,
            new EventFactory<Order>() {
                @Override
                public Order newInstance() {
                    return new Order();
                }
            },
            1024 * 1024,
            new YieldingWaitStrategy());
        
        // 2.通过ringBuffer创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        final CountDownLatch consumeEnd = new CountDownLatch(10000);
        // 3.创建多个消费者数组
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("C" + i, consumeEnd);
        }
        
        // 4.构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<Order>(
            ringBuffer,
            sequenceBarrier,
            new EventExceptionHandler(),
            consumers
        );
        
        // 5.设置多个消费者的sequence序号 用于单独统计消费进度，并且设置到ringbuffer中
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        
        // 6.启动workerPool
        workerPool.start(es);
        
        final CountDownLatch createThreadEnd = new CountDownLatch(100);
        
        // 100个生产者
        for (int i = 0; i < 100; i++) {
            final Producer producer = new Producer(ringBuffer);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        producer.sendData(UUID.randomUUID().toString());
                    }
                }
            }).start();
            createThreadEnd.countDown();
        }
        
        createThreadEnd.await();
        System.err.println("------------线程创建完毕，开始生产数据------------");
        
        consumeEnd.await(); // 等待消费者消费完生产者的数据
        System.err.println("任务总数：" + consumers[2].getCount());
        es.shutdown();
    }
    
    static class EventExceptionHandler implements ExceptionHandler<Order> {
        
        @Override
        public void handleEventException(Throwable ex, long sequence, Order event) {
        
        }
        
        @Override
        public void handleOnStartException(Throwable ex) {
        
        }
        
        @Override
        public void handleOnShutdownException(Throwable ex) {
        
        }
    }
}
