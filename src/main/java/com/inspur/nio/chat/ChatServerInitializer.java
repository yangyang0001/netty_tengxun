package com.inspur.nio.chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * User: YANG
 * Date: 2019/5/31-17:24
 * Description: No Description
 */
public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();



        //http 请求的 handler
        channelPipeline.addLast(new HttpServerCodec());
        channelPipeline.addLast(new HttpObjectAggregator(65535));
        channelPipeline.addLast(new ChunkedWriteHandler());
        channelPipeline.addLast(new MyHttpChatServerHandler());

        //websocket 请求的 handler
//        channelPipeline.addLast(new ChunkedWriteHandler()); 放到上面一行
        //这里是webSocket 的访问的根路径 ws://server:port/context_path/ws
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        channelPipeline.addLast(new MyWebSocketFrameHandler());

        //自定义协议的 handler


    }
}
