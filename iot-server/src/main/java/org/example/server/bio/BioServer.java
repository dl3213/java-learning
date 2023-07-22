package org.example.server.bio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单bio
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BioServer {
    @Value("${bio.server-ip}")
    private String serverIp;
    @Value("${bio.server-port}")
    private int serverPort;
    private ExecutorService newCachedThreadPool;
    private ServerSocket serverSocket;

    @Async
    public void start() {
        try {
            newCachedThreadPool = Executors.newCachedThreadPool();
            serverSocket = new ServerSocket(this.serverPort);
            log.info("bio server started in {} ", this.serverPort);

            while (true) {
                Socket socket = serverSocket.accept();
                int clientPort = socket.getPort();
                InetAddress clientAddress = socket.getInetAddress();
                log.info("new client => {}:{}", clientAddress, clientPort);

                newCachedThreadPool.execute(() -> {
                    try {
                        byte[] bytes = new byte[1024];
                        InputStream inputStream = socket.getInputStream();

                        while (true) {
                            int read = inputStream.read(bytes);
                            if (read != 1) {
                                log.info("from cline[{}:{}]:{}", clientAddress, clientPort, new String(bytes, 0, read));
                            } else {
                                break;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        log.info("client disconnect");
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
