package com.inspur.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * User: YANG
 * Date: 2019/5/30-22:31
 * Description: No Description
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress("localhost", 9999);
        socketChannel.configureBlocking(false);
        socketChannel.connect(socketAddress);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while(true){
            selector.select();//阻塞等待事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isConnectable()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    if(channel.isConnectionPending()){
                        channel.finishConnect();
                        byte[] bytes = UUID.randomUUID().toString().getBytes("UTF-8");
                        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
                        byteBuffer.put(bytes);
                        byteBuffer.flip();
                        channel.write(byteBuffer);
                        channel.register(selector, SelectionKey.OP_WRITE);
                    }
                } else if(selectionKey.isWritable()){
                    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line = bufferedReader.readLine();
                    byte[] bytes = line.getBytes("UTF-8");

                    ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
                    byteBuffer.put(bytes);
                    byteBuffer.flip();

                    socketChannel.write(byteBuffer);
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                }
                iterator.remove();
            }

        }


    }
}
