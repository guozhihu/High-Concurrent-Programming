实战应用业务场景  
Disruptor与Netty实现百万级场链接接入<br>
Disruptor与Netty网络通信框架整合提升性能<br>
1.在使用Netty进行接收处理数据的时候，我们尽量都不要在工作线程上编写自己的代码逻辑<br>
2.我们需要利用异步机制，比如使用线程池异步处理，如果使用线程池就意味着使用阻塞队列，这里可以替换为Disruptor提高性能<br>
分布式统一ID生成策略抗压