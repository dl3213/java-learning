package me.sibyl.netty.client;

public class ClientStarter {
    public static void main(String[] args) throws Exception {
        System.out.println("client start...");
        String ip = "127.0.0.1";
        int port = 8082;
        new TestClient(ip, port).start();
    }
}
