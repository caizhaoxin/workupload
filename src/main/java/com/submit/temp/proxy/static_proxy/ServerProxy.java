package com.submit.temp.proxy.static_proxy;

import com.submit.temp.proxy.static_proxy.Server;

public class ServerProxy implements Server {
    private Server server;

    public ServerProxy(Server server) {
        this.server = server;
    }

    @Override
    public int add(int a, int b) {
        System.out.println("before invode");
        int ans = this.server.add(a,b);
        System.out.println("after invode");
        return ans;
    }
}
