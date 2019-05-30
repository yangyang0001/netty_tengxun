package com.inspur.bio;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * User: YANG
 * Date: 2019/5/30-10:41
 * Description: No Description
 */
public class BIOServer {

    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9999);
            while(true){
                Socket socket = serverSocket.accept();
                byte[] bytes = new byte[1024];
                int readCount = socket.getInputStream().read(bytes);
                String line = new String(bytes,0, readCount, Charset.forName("UTF-8"));
                System.out.println(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }





    }
}
