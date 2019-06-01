package com.inspur.nio.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.io.RandomAccessFile;

/**
 * User: YANG
 * Date: 2019/5/31-17:29
 * Description: No Description
 */
public class MyHttpChatServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final String BASE_PATH =
            MyHttpChatServerHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1) + "webroot";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uri = request.uri();

            String fileName = uri;

            String contentType = "";
            if (uri.endsWith("/")) {
                contentType = "text/html;";
                fileName = "/chat.html";
            } else if (uri.endsWith(".css")) {
                contentType = "text/css;";
            } else if (uri.endsWith(".js")) {
                contentType = "text/javascript;";
            } else if (uri.toLowerCase().matches("(jpg|png|gif)$")) {
                String ext = uri.substring(uri.lastIndexOf("."));
                contentType = "image/" + ext;
            } else if (uri.endsWith("favicon.ico")) {
                return;
            } else if (uri.endsWith("/ws")){
                ctx.fireChannelRead(msg);
                return;
            }
            System.out.println("BASE_PATH + fileName ---------->:" + BASE_PATH + fileName);

            RandomAccessFile file = new RandomAccessFile(BASE_PATH + fileName, "r");
            byte[] bytes = new byte[(int) file.length()];
            file.read(bytes, 0, (int) file.length());

            ByteBuf content = Unpooled.copiedBuffer(bytes);
            HttpResponse response = new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType + "charset=UTF-8;");

            boolean keepAlive = HttpHeaders.isKeepAlive(request);
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.writeAndFlush(response);
            file.close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyHttpChatServerHandler.class ----------------->:channelActive");
        super.channelActive(ctx);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyHttpChatServerHandler.class ----------------->:channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyHttpChatServerHandler.class ----------------->:handlerAdded");
        super.handlerAdded(ctx);
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyHttpChatServerHandler.class ----------------->:handlerRemoved");
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }



}
