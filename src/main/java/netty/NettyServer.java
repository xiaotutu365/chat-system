package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyServer {
    public static void main(String[] args) {
        // 定义一对线程组
        // 主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 从线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            // netty服务器的创建，ServerBootstrap是一个启动类
            bootstrap.group(bossGroup, workerGroup)     // 设置主从线程组
                    .channel(NioServerSocketChannel.class)  // 设置NIO双向通道
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("HttpServerCodec", new HttpServerCodec());
                            pipeline.addLast(new CustomHandler());
                        }
                    });

            // 启动server，并且设置8088为启动的端口号，同时启动方式为同步
            ChannelFuture future = bootstrap.bind(8888).sync();

            // 监听关闭的channel，设置为同步方式
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}