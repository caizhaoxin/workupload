package com.submit.temp.proxy.dynamic_proxy;

public class MyServer implements Server {
    private String name;

    public MyServer() {
    }

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}