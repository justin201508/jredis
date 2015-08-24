package com.jredis.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class JRedisCommandHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
		//读取socket数据到字节数组 
		//[对于aio/nio来说，框架会自动完成就绪的socket的数据封装到Buffer的工作callback方法需要做的就是如何处理这些数据]
		ByteBuf buf = (ByteBuf)msg;
		byte[] reqdata = new byte[buf.readableBytes()];
		buf.readBytes(reqdata);
		
		String reqStr = new String(reqdata,"utf-8");
		System.out.println("Hexin time server receive  order : " + reqStr);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(reqStr) 
				? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
		
				
		System.out.println("Hexin time server receive  order : " + reqStr);
		//处理完read data之后，就可以将响应的数据write到socketfd中
		ByteBuf respBuf = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(respBuf);
		
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
}
