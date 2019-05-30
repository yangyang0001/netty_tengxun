package com.inspur.bio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: YANG
 * Date: 2019/5/30-10:24
 * Description: No Description
 */
public class ReadFileTest {

    public static void main(String[] args){
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream("info.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            String line = null;
            while((line = bufferedReader.readLine()) != null){
                System.out.println(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
