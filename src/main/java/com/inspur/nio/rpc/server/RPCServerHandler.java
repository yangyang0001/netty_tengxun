package com.inspur.nio.rpc.server;

import com.inspur.nio.rpc.InvokeMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: YANG
 * Date: 2019/6/2-15:42
 * Description: No Description
 */
public class RPCServerHandler extends ChannelInboundHandlerAdapter {

    private ConcurrentHashMap<String, Object> interfaceMap = new ConcurrentHashMap<String, Object>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InvokeMessage message = (InvokeMessage) msg;
        Object object = interfaceMap.get(message.getTargetInterface());
        Method method = object.getClass().getMethod(message.getTargetMethod(), message.getParamsTypes());
        Object result = method.invoke(object, message.getObjects());
        ctx.writeAndFlush(result);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        ctx.close();
    }

    public RPCServerHandler(){
        try {
            doRegister("com.inspur.nio.rpc.provider");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    //模仿 dubbo 注册服务端的接口 和 实例
    private void doRegister(String packageName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for ( File file : dir.listFiles()) {
            if(file.isDirectory()){
                doRegister(packageName + "." + file.getName());
            }else{
                String clazzName = packageName + "." + file.getName().replace(".class", "").trim();
                Class<?> clazz = this.getClass().getClassLoader().loadClass(clazzName);
                Class<?> interfaces = clazz.getInterfaces()[0];
                System.out.println("clazzName -------------->:" + clazzName +
                        ", interfaces.toString() -------->:" + interfaces.toString() +
                        ", interfaces.getName() --------->:" + interfaces.getName());
                interfaceMap.putIfAbsent(interfaces.getName(), clazz.newInstance());
            }
        }
    }


}
