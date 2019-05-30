package com.inspur.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * User: YANG
 * Date: 2019/5/30-10:41
 * Description: No Description
 */
public class BIOClient {

    public static void main(String[] args){

        try {
            Socket socket = new Socket("localhost", 9999);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("Hello".getBytes(Charset.forName("UTF-8")));
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }


    }
}
