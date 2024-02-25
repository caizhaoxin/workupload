package com.submit.temp.proxy.dynamic_proxy;

import com.submit.temp.proxy.dynamic_proxy.MyServer;
import com.submit.temp.proxy.dynamic_proxy.Server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 1、在RPC方法执行前需要有消息序列化、网络等操作
        // 2、在RPC方法执行后需要反序列化，网络发包等操作
        // 3、如果在编写每个方法时都插入序列和反序列的方法，会显得非常冗余
        // 4、因此我们需要加入代理类，来辅助我们完成这样的事情
        Server server = new MyServer();
        Server myServerProxy = (Server) new ProxyFactory(server).getProxyInstance();
        System.out.println(myServerProxy.add(1,2));
    }
}
