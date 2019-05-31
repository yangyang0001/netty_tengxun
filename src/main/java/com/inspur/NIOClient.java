package com.inspur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: YANG
 * Date: 2019/5/31-10:23
 * Description: No Description
 * 重新定义NIOClient,用线程池处理向Server端写数据
 */
public class NIOClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 9999);
        socketChannel.connect(inetSocketAddress);

        Selector selector  = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while(true){
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isConnectable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    if(channel.isConnectionPending()){  //这个if 和 finishConnect() 这两行是必须要进行的,否则会抛异常,"main" java.nio.channels.NotYetConnectedException
                        channel.finishConnect();
                        byte[] bytes = UUID.randomUUID().toString().getBytes(Charset.forName("UTF-8"));
                        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
                        byteBuffer.put(bytes);
                        byteBuffer.flip();
                        channel.write(byteBuffer);

                        channel.register(selector, SelectionKey.OP_WRITE);
                        iterator.remove();
                    }
                } else if(selectionKey.isWritable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    CountDownLatch countDownLatch = new CountDownLatch(1);
                    executorService.execute(() -> {
                        InputStreamReader reader = new InputStreamReader(System.in);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        try {
                            String line = bufferedReader.readLine();
                            byte[] bytes = line.getBytes(Charset.forName("UTF-8"));
                            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                            writeBuffer.put(bytes);
                            writeBuffer.flip();
                            channel.write(writeBuffer);
                            countDownLatch.countDown();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    countDownLatch.await();
                    channel.register(selector, SelectionKey.OP_WRITE);
                    iterator.remove();
                }
            }
        }
    }
}
