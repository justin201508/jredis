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
				//ָ��server socket channel ������ΪNioServerSocketChannel
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)// specify the channel max size
				// this will register the channel handler to server
				// ����reactorģʽ�е�handler�࣬���ڴ�������IO�¼��������¼��־������Ϣ���б����ȵ�
				.childHandler(new JRedisCommandChannelHandler());
			
			//ChannelFuture ������java.util.concurrent.Future,�����첽IO��֪ͨ�ص�
			//�󶨶˿ڵȴ��ص�
			System.out.println("server bind port...");
			ChannelFuture future = serverBoot.bind(port).sync();
			
			//�ȴ�����˼����˿ڹر�,�����������������������У��ȴ���������·�ر�֮��Ż᷵�أ�ͬʱ��������main�����˳�
			System.out.println("server to close future and sync ...");
			future.channel().closeFuture().sync();
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			//������ʱ���ͷ�ϵͳ��Դ
			acceptorGroup.shutdownGracefully();
			ioGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {

	}

}
