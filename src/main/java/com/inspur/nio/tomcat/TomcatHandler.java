package com.inspur.nio.tomcat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * User: YANG
 * Date: 2019/5/31-13:34
 * Description: No Description
 */
public class TomcatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest) msg;
            MyRequest myRequest = new MyRequest(ctx, httpRequest);
            MyResponse myResponse = new MyResponse(ctx, myRequest);

            String method = httpRequest.method().name();
            if("GET".equalsIgnoreCase(method)){
                System.out.println("tomcat server 接收到数据....");
                //模仿 调用我们的 servlet 返回数据
                new MyServlet().doGet(myRequest, myResponse);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
