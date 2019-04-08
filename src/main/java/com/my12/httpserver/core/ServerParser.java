package com.my12.httpserver.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 解析server.xml配置文件
 * @author my12
 */
public class ServerParser {



    /**
     * 获取服务器的端口号
     * @return int port
     */
    public static int getPort(){
        //设置服务器默认端口 8080
        int port = 8080;
        try {
            //创建解析器
            SAXReader saxReader = new SAXReader();
            //通过解析器的read方法将配置文件读取到内存中，生成一个document对象
            Document document = saxReader.read("src/main/resources/server.xml");

            //获取connector节点元素的xpath路径: //connector
            Element connectorEle = (Element) document.selectSingleNode("//connector");

            //获取port属性的值
            port = Integer.parseInt(connectorEle.attributeValue("port"));

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return port;
    }
}
