package com.my12.httpserver.core;

import com.my12.httpserver.utils.Logger;
import org.dom4j.DocumentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 主程序
 *
 * @author my12
 */
public class BootStrap {
    public static void main(String[] args) {
        //程序入口
        start();
    }

    /**
     * 主程序入口
     */
    public static void start() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader bufferedReader = null;

        try {
            Logger.log("httpserver start");
            //获取当前时间
            long start = System.currentTimeMillis();

            //解析服务器中包含的web.xml文件, 这里的webAppNames写死了，理应需要一个方法获取webAppNames,有待实现
            String[] webAppNames = {"page","bank"};
            WebParser.parser(webAppNames);

            //获取系统端口号
            int port = ServerParser.getPort();
            Logger.log("httpserver-port: " + port);

            //创建服务器套接字
            serverSocket = new ServerSocket(port);

            //获取结束时间
            long end = System.currentTimeMillis();
            Logger.log("httpserver started: " + (end - start) + " ms");

            while (true) {

                //开始监听网络，此时服务器处于等待状态，等待接收客户端的消息
                clientSocket = serverSocket.accept();

/*                //接收客户端消息
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String temp = null;

                while ((temp = bufferedReader.readLine()) != null) {
                    System.out.println(temp);
                }*/

                //开启线程
                new Thread(new HandlerRequest(clientSocket)).start();
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
/*            if (bufferedReader != null) {
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
            }*/
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
