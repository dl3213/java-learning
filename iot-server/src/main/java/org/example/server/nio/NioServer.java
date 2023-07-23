package org.example.server.nio;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * nio 三大组件：selector,channel,buffer
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "nio", name = "enable", havingValue = "true", matchIfMissing = false)
public class NioServer {

    @Value("${nio.server-ip}")
    private String serverIp;
    @Value("${nio.server-port}")
    private int serverPort;

    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            Selector selector = Selector.open();

            serverSocketChannel.socket().bind(new InetSocketAddress(this.serverPort));
            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select(1000) == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {

                        SocketChannel socketChannel = serverSocketChannel.accept();
                        log.info("new client => {}", socketChannel.hashCode());
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    }
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();// 得到关联的channel
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment(); //得到关联的共享数据
                        channel.read(byteBuffer);
                        log.info("from client => {}", new String(byteBuffer.array()));
                    }
                    iterator.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
