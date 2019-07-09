package com.disruptor.netty.server;

import com.disruptor.netty.codec.MarshallingCodeCFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Author: zhihu
 * Description: 服务器端
 * Date: Create in 2019/4/23 10:35
 */
public class NettyServer {
    
    public void bind(int port) {
        // 1.创建两个工作线程组：一个用于接收网络请求的线程组，另一个用于实际处理业务的线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        
        // 2.辅助类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                // 设置tcp缓冲区，设置SYNC队列和Accept队列的大小，这里支持每秒钟接入1024个链接
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 表示缓存区动态调配（自适应），接收数据包的缓存大小，采用如下自适应会有性能问题，该种策略适合每次接收的数据包的大小差不多一样大
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                // 缓存区 池化操作
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                // 添加netty打印日志
                .handler(new LoggingHandler(LogLevel.INFO))
                // 处理接收数据，由workGroup线程组处理
                .childHandler(new ChildChannelHandler());
            
            // 绑定端口，同步等待请求连接
            ChannelFuture cf = serverBootstrap.bind(port).sync();
            System.err.println("Server Startup...");
            // 等待服务端监听端口关闭
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅停机
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.err.println("Server ShutDown...");
        }
    }
    
    /**
     * 网络事件处理器
     */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            // 添加Jboss的序列化，编解码工具
            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder()); // 对接收到的二进制数据进行解码
            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder()); // 发送给客户端的数据进行编码
            // 处理网络IO，接收到数据后交由ServerHandler处理
            socketChannel.pipeline().addLast(new ServerHandler());
        }
    }
}
