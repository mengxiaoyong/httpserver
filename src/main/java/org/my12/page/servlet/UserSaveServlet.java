package org.my12.page.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 * @author my12
 */
public class UserSaveServlet implements Servlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) {
        //获取页面请求提交参数的值
        String username = request.getParameterValue("username");
        String gender = request.getParameterValue("gender");
        String[] interest = request.getParameterValues("interest");

        StringBuilder interests = new StringBuilder();

        for (String interestValue : interest) {
            interests.append(interestValue).append(" ");
        }

        //获取响应流对象
        PrintWriter out = response.getWriter();
        out.print("<html>");
        out.print("<head>");
        out.print("<title>用户信息</title>");
        out.print("<meta content='text/html;charset=utf-8'/>");
        out.print("</head>");
        out.print("<body>");
        out.print("用户名: "+username + "<br>");
        out.print("性别: "+gender+ "<br>");
        out.print("兴趣: "+interests + "<br>");

        out.print("</body>");
        out.print("</html>");

    }
}
