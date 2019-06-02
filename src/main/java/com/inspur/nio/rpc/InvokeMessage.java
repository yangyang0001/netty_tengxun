package com.inspur.nio.rpc;

import java.io.Serializable;

/**
 * User: YANG
 * Date: 2019/6/2-15:35
 * Description: No Description
 */
public class InvokeMessage implements Serializable {
    private static final long serialVersionUID = 5858831982269930148L;

    private String targetInterface;
    private String targetMethod;
    private Class<?>[] paramsTypes;
    private Object[] objects;

    public String getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(String targetInterface) {
        this.targetInterface = targetInterface;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Class<?>[] getParamsTypes() {
        return paramsTypes;
    }

    public void setParamsTypes(Class<?>[] paramsTypes) {
        this.paramsTypes = paramsTypes;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }
}
