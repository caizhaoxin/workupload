package com.submit.temp.proxy.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {
    private Object server;

    public ProxyFactory(Object server) {
        this.server = server;
    }

    // 为目标对象生成代理对象
    public Object getProxyInstance() {
        return Proxy.newProxyInstance(server.getClass().getClassLoader(), server.getClass().getInterfaces(),
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("before invode");

                        // 执行目标对象方法
                        Object returnValue = method.invoke(server, args);

                        System.out.println("after invode");
                        return returnValue;
                    }
                });
    }
}
