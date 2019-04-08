package com.my12.httpserver.core;

import com.my12.httpserver.utils.Logger;

import javax.servlet.Servlet;
import java.io.*;
import java.net.Socket;
import java.util.Map;

/**
 * 多线程处理客户端请求
 *
 * @author my12
 */
public class HandlerRequest implements Runnable {

    public Socket clientSocket;

    public HandlerRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        //处理客户端请求
        BufferedReader bufferedReader = null;
        PrintWriter out = null;
        //在日志打印当前线程名称
        Logger.log("httpserver thread: " + Thread.currentThread().getName());


        try {
            //接收客户端消息
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //获取响应流对象
            out = new PrintWriter(clientSocket.getOutputStream());

            //获取请求协议的请求行
            String requestLine = bufferedReader.readLine(); //GET /a/a.html HHTP/1.1
            //获取URI -> 请求行 ->请求方式 请求协议版本号 ->三者之间通过空格惊醒连接

            //通过split函数获取请求URI ,请求URI是第二个，所以下标写1
            String requestURI = requestLine.split(" ")[1]; //{"GET","/a/a.html","HTTP/1.1"}

            System.out.println(requestURI);

            //判断用户请求是否为一个静态页面: 以.html或.htm结尾的文件叫html页面
            if (requestURI.endsWith(".html") || requestURI.endsWith(".htm")) {
                //处理静态页面的方法
                responseStaticPage(requestURI, out);
            } else {//动态资源： java程序,业务处理类
                //requestURI: /page/login?username=zs&pwd=123
                //requestURI: /page/login

                String servletPath = requestURI;
                System.out.println("servletPath: " + servletPath);

                if ("/favicon.ico".equals(servletPath)) {
                    return;
                }
                //判断servletPath是否包含参数
                if (servletPath.contains("?")) {
                    servletPath = servletPath.split("[?]")[0];// /page/login
                }

/*                //这个代码耦合严重
                if("/page/login".equals(servletPath)){

                    LoginServlet loginServlet = new LoginServlet();
                    loginServlet.service();
                }*/

                //获取应用名称： page在url 里面： /page/login
                String webAppName = servletPath.split("[/]")[1];
                //获取servletMaps集合中的value -> servletMap -> key: urlPattern value: servletClassName
                Map<String, String> servletMap = WebParser.servletMaps.get(webAppName);

                //获取servletMap集合中的key值 -> 存在于url中 /page/login  /page/use/xxx/xxx/xxx/....
                String urlPattern = servletPath.substring(1 + webAppName.length());
                //获取servletClassName
                String servletClassName = servletMap.get(urlPattern);


                //判断该业务处理的servlet类是否存在
                if (servletClassName != null) {
                    //获取封装响应参数对象
                    ResponseObject responseObject = new ResponseObject();
                    responseObject.setWriter(out);

                    //获取封装请求参数
                    RequestObject requestObject = new RequestObject(requestURI);


                    out.print("HTTP/1.1 200 OK\n");
                    out.print("Content-Type:text/html;charset=utf-8\n\n");

                    //创建Servlet对象之前，先从缓存池中查找
                    //1.有：拿来直接使用
                    //2.没有：创建Servlet对象，放到缓存池中
                    Servlet servlet = ServletCache.get(urlPattern);
                    if (servlet == null) {
                        //通过反射机制创建该业务处理类
                        Class c = Class.forName(servletClassName);
                        Object obj = c.newInstance();
                        //这时候，服务器开发人员不知道如何调用servlet业务处理类的方法？
                        servlet = (Servlet) obj;
                        //将创建好的Servlet对象放到缓存池中
                        ServletCache.put(urlPattern,servlet);
                    }

                    Logger.log("Servlet对象: " + servlet);
                    servlet.service(requestObject, responseObject);

                } else {//找不到该业务的处理类

                    //404找不到资源，调用PageNotFound函数
                    StringBuffer html = PageNotFound();
                    out.print(html);
                }

            }

            //强制刷新
            out.flush();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理静态页面
     *
     * @param requestURI 请求url
     * @param out        响应流对象
     */
    public void responseStaticPage(String requestURI, PrintWriter out) {

        //requestURI : /page/index.html
        //静态页面的路径: page/index.html
        //去掉page前面的斜杠 /
        String htmlPath = requestURI.substring(1);

        BufferedReader bufferedReader = null;

        try {
            //读取页面
            bufferedReader = new BufferedReader(new FileReader(htmlPath));

            StringBuilder html = new StringBuilder();
            //拼接响应信息
            html.append("HTTP/1.1 200 OK\n");
            html.append("Content-Type:text/html;charset=utf-8\n\n");


            String temp = null;

            while ((temp = bufferedReader.readLine()) != null) {
                html.append(temp);
            }

            //输出html
            out.print(html);

        } catch (FileNotFoundException e) {
            //404找不到资源，调用PageNotFound函数
            StringBuffer html = PageNotFound();
            out.print(html);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 找不到资源的响应界面
     *
     * @return StringBuffer
     */
    public StringBuffer PageNotFound() {
        StringBuffer html = new StringBuffer();
        html.append("HTTP/1.1 404 NotFound\n");
        html.append("Content-Type:text/html;charset=utf-8\n\n");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>404-错误</title>");
        html.append("<meta content='text/html;charset=utf-8'/>");
        html.append("</head>");
        html.append("<body>");
        html.append("<center><font size='35px' color='red'>404 NotFound</font></center>");
        html.append("</body>");
        html.append("</html>");

        return html;
    }


}
