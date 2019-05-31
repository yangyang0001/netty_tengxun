package com.inspur.nio.tomcat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * User: YANG
 * Date: 2019/5/31-13:36
 * Description: No Description
 */
public class MyRequest {

    private ChannelHandlerContext channelHandlerContext;
    private HttpRequest httpRequest;

    public MyRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        this.channelHandlerContext = ctx;
        this.httpRequest = httpRequest;
    }

    public String getURI(){
        return this.httpRequest.uri();
    }

    public Map<String,List<String>> getParams(){
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(getURI());
        Map<String,List<String>> params = queryStringDecoder.parameters();
        return params;
    }


}
