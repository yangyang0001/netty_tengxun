package com.inspur.nio.rpc.provider;

import com.inspur.nio.rpc.api.SayHelloService;

/**
 * User: YANG
 * Date: 2019/6/2-15:39
 * Description: No Description
 */
public class SayHelloServiceImpl implements SayHelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
