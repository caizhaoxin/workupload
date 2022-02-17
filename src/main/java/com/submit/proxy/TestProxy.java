package com.submit.proxy;


public class TestProxy {
    public static void main(String[] args) {
        testDynamicProxy();
    }

    public static void testDynamicProxy() {
        IUserDao target = new UserDao();
        System.out.println(target.getClass());  //输出目标对象信息
        IUserDao proxy = (IUserDao) new UserProxyFactory(target).getProxyInstance();
        System.out.println(proxy.getClass());  //输出代理对象信息
        proxy.save();  //执行代理方法
    }
}