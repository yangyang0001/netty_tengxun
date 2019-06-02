package com.inspur.nio.rpc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * User: YANG
 * Date: 2019/6/2-15:33
 * Description: No Description
 */
public class RPCServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        //处理的拆包、粘包的解、编码器
        channelPipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
        channelPipeline.addLast("frameEncoder",new LengthFieldPrepender(4));

        //处理序列化的解、编码器（JDK默认的序列化）
        channelPipeline.addLast("encoder",new ObjectEncoder());
        channelPipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

        channelPipeline.addLast(new RPCServerHandler());
    }
}
