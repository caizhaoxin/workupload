package com.submit.temp.proxy.static_proxy;

import com.submit.temp.proxy.static_proxy.MyServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 1、在RPC方法执行前需要有消息序列化、网络等操作
        // 2、在RPC方法执行后需要反序列化，网络发包等操作
        // 3、如果在编写每个方法时都插入序列和反序列的方法，会显得非常冗余
        // 4、因此我们需要加入代理类，来辅助我们完成这样的事情
        Server server = new MyServer();
        Server serverProxy = new ServerProxy(server);
        System.out.println("直接调用");
        System.out.println(serverProxy.add(1, 2));

        System.out.println("--------------------------");
        System.out.println("反射调用");
        Class<?> myServerClass = serverProxy.getClass();
        //通过getMethon方法取得setName这个方法的实例化对象，方法名称与参数类型
        Method setMethod = myServerClass.getDeclaredMethod("add", int.class, int.class);
        //通过invoke调用方法
        Integer integer = (Integer) setMethod.invoke(serverProxy, 1,2);
        System.out.println(integer);
    }
}
