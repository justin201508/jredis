package com.jredis.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class JRedisCommandHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
		//��ȡsocket���ݵ��ֽ����� 
		//[����aio/nio��˵����ܻ��Զ���ɾ�����socket�����ݷ�װ��Buffer�Ĺ���callback������Ҫ���ľ�����δ�����Щ����]
		ByteBuf buf = (ByteBuf)msg;
		byte[] reqdata = new byte[buf.readableBytes()];
		buf.readBytes(reqdata);
		
		String reqStr = new String(reqdata,"utf-8");
		System.out.println("Hexin time server receive  order : " + reqStr);
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(reqStr) 
				? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
		
				
		System.out.println("Hexin time server receive  order : " + reqStr);
		//������read data֮�󣬾Ϳ��Խ���Ӧ������write��socketfd��
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
