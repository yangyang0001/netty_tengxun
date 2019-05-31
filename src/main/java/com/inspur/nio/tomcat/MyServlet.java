package com.inspur.nio.tomcat;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * User: YANG
 * Date: 2019/5/31-14:29
 * Description: No Description
 */
public class MyServlet{

    protected void doGet(MyRequest request, MyResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(MyRequest request, MyResponse response) throws ServletException, IOException {
        response.write();
    }
}
