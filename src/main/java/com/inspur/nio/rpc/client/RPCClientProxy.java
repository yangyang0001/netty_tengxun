package com.inspur.nio.rpc.client;

import com.inspur.nio.rpc.InvokeMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * User: YANG
 * Date: 2019/6/2-15:57
 * Description: No Description
 */
public class RPCClientProxy {
    public static <T> T getInstance(Class<?> interfaces){
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{interfaces}, new MethodProxy(interfaces));
    }
}

//做个 伪代理
class MethodProxy implements InvocationHandler{

    private Class<?> clazz;

    public MethodProxy(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(Object.class == method.getDeclaringClass()){
            return method.invoke(this, args);
        } else {
            InvokeMessage message = new InvokeMessage();
            message.setTargetInterface(clazz.getName());
            message.setTargetMethod(method.getName());
            message.setParamsTypes(method.getParameterTypes());
            message.setObjects(args);
            return doRPC(message);
        }
    }

    private Object doRPC(InvokeMessage message){
        EventLoopGroup group = new NioEventLoopGroup();
        RPCClientHandler handler = new RPCClientHandler();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline channelPipeline = ch.pipeline();
                            //处理的拆包、粘包的解、编码器
                            channelPipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
                            channelPipeline.addLast("frameEncoder",new LengthFieldPrepender(4));

                            //处理序列化的解、编码器（JDK默认的序列化）
                            channelPipeline.addLast("encoder",new ObjectEncoder());
                            channelPipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                            channelPipeline.addLast(handler);
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 8888).sync();
            //客户端发送数据
            channelFuture.channel().writeAndFlush(message).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

        return handler.getObject();
    }
}
