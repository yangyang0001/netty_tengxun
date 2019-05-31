package com.inspur.nio.tomcat;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;

/**
 * User: YANG
 * Date: 2019/5/31-13:36
 * Description: No Description
 */
public class MyResponse {

    private ChannelHandlerContext channelHandlerContext;
    private MyRequest myRequest;

    public MyResponse(ChannelHandlerContext ctx, MyRequest myRequest) {
        this.channelHandlerContext = ctx;
        this.myRequest = myRequest;
    }

    public void write() throws UnsupportedEncodingException {
        byte[] bytes = JSON.toJSONString(myRequest.getParams()).getBytes("UTF-8");
        ByteBuf content = Unpooled.copiedBuffer(bytes);
        FullHttpResponse response =
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, content);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

        this.channelHandlerContext.writeAndFlush(response);
    }
}
