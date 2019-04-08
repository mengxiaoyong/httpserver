package org.my12.bank.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author my12
 */
public class ActTransferServlet implements Servlet {

    @Override
    public void service(ServletRequest request, ServletResponse response) {
        //获取页面请求参数
        String actForm = request.getParameterValue("actForm");
        double balance = Double.parseDouble(request.getParameterValue("balance"));

        String actTo = request.getParameterValue("actTo");
        System.out.println(actForm);

        //连接数据库
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            //注册驱动
            Class.forName("com.mysql.jdbc.Driver");

            //获取数据库连接
            String url = "jdbc:mysql://127.0.0.1:3306/test";
            String user = "root";
            String password = "123456";
            conn = DriverManager.getConnection(url, user, password);

            //开启事务，关闭自动提交
            conn.setAutoCommit(false);

            //定义sql语句框架
            String sql_form = "update t_act set balance = balance -? where actno =?";

            //进行sql语句预编译
            ps = conn.prepareStatement(sql_form);
            //进行赋值
            ps.setDouble(1, balance);
            ps.setString(2, actForm);
            //执行sql语句
            count = ps.executeUpdate();



            String sql_to = "update t_act set balance = balance + ? where actno =?";
            //进行sql语句预编译
            ps = conn.prepareStatement(sql_to);
            //进行赋值
            ps.setDouble(1, balance);
            ps.setString(2, actTo);
            count = count + ps.executeUpdate();

            //提交事务
            conn.commit();



        } catch (ClassNotFoundException | SQLException e) {

            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            //关闭资源
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        PrintWriter out = response.getWriter();

        if (count == 2){
            out.print("<html>");
            out.print("<head>");
            out.print("</head>");
            out.print("<title>银行转账结果</title>");
            out.print("<meta content='text/html;charset=utf-8'/>");
            out.print("<body>");
            out.print("<center><font size='35px' color='green'>转账成功</font></center>");

            out.print("</body>");

            out.print("</html>");

        }else {
            out.print("<html>");
            out.print("<head>");
            out.print("</head>");
            out.print("<title>银行转账结果</title>");
            out.print("<meta content='text/html;charset=utf-8'/>");
            out.print("<body>");
            out.print("<center><font size='35px' color='red'>转账失败</font></center>");

            out.print("</body>");

            out.print("</html>");
        }

    }
}
