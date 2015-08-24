package com.jredis.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.jredis.server.handler.JRedisCommandChannelHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;



public class JRedisServer {
	
	static final Logger LOGGER = LoggerFactory.getLogger(JRedisServer.class);
	
	int port;

	public JRedisServer(int port) {
		this.port = port;
		// proactor event loop pool
		EventLoopGroup acceptorGroup = new NioEventLoopGroup();
		// io handler pool
		EventLoopGroup ioGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBoot = new ServerBootstrap();
			serverBoot.group(acceptorGroup, ioGroup) 
				//指定server socket channel 的类型为NioServerSocketChannel
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)// specify the channel max size
				// this will register the channel handler to server
				// 类似reactor模式中的handler类，用于处理网络IO事件，比如记录日志，对消息进行编解码等等
				.childHandler(new JRedisCommandChannelHandler());
			
			//ChannelFuture 类似于java.util.concurrent.Future,用于异步IO的通知回调
			//绑定端口等待回调
			System.out.println("server bind port...");
			ChannelFuture future = serverBoot.bind(port).sync();
			
			//等待服务端监听端口关闭,这个方法将会阻塞程序的运行，等待服务器链路关闭之后才会返回，同时服务器随main函数退出
			System.out.println("server to close future and sync ...");
			future.channel().closeFuture().sync();
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			//结束的时候释放系统资源
			acceptorGroup.shutdownGracefully();
			ioGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {

	}

}
