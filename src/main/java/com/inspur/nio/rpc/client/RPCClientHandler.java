package com.inspur.nio.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * User: YANG
 * Date: 2019/6/2-15:33
 * Description: No Description
 */
public class RPCClientHandler extends ChannelInboundHandlerAdapter {

    private boolean dataFlag = false;
    private Object object;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.object = msg;
    }

    /**
     * 这个方法必须重写,否则 延迟处理 返回结果!
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        this.dataFlag = true;
        ctx.flush();
        ctx.close();
    }

    public boolean getDataFlag() {
        return dataFlag;
    }

    public void setDataFlag(boolean dataFlag) {
        this.dataFlag = dataFlag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
