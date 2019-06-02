package com.inspur.nio.rpc.client;

import com.inspur.nio.rpc.api.SayHelloService;

/**
 * User: YANG
 * Date: 2019/6/2-15:56
 * Description: No Description
 */
public class RPCClient {

    public static void main(String[] args){
        SayHelloService sayHelloService = RPCClientProxy.getInstance(SayHelloService.class);
        System.out.println(sayHelloService.sayHello("YangYang"));
    }
}
