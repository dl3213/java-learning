package me.sibyl.netty.server;

public class ServerStarter {
    public static void main(String[] args) throws Exception {
        System.out.println("server start...");
        int port = 8081;
        new TestServer(port).start();
    }
}

