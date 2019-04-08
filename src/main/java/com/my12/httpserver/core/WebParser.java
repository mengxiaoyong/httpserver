package com.my12.httpserver.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 解析服务器中的web.xml配置文件
 *
 * @author my12
 */
public class WebParser {

    public static Map<String,Map<String,String>> servletMaps = new HashMap<String, Map<String, String>>();

    /**
     * 解析服务器中所有应用的webxml
     * @param webAppNames 服务器中所有应用名称
     */
    public static void parser(String[] webAppNames) throws DocumentException {
        for (String webAppName : webAppNames) {

            Map<String,String> servletMap = parser(webAppName);

            servletMaps.put(webAppName,servletMap);
        }
    }


    /**
     * 解析单个应用的webxml配置文件
     *
     * @param webAppName 应用名称
     * @return servletMap<String , String>
     */
    public static Map parser(String webAppName) throws DocumentException {
        //获取web.xml路径
        String webPath = webAppName + "/WEB-INF/web.xml";
        //创建解析器
        SAXReader saxReader = new SAXReader();
        //通过解析器的read方法将配置文件读取到内存中，生成一个document对象
        Document document = saxReader.read(new File(webPath));

        //获取servlet节点元素: web-app -> servlet
        List<Element> servletNodes = document.selectNodes("/web-app/servlet");
        //创建一个servletInfoMap集合，将servlet-name和servlet-class的值分别当作key和value
        HashMap<String, String> servletInfoMap = new HashMap<String, String>();
        //开始遍历
        for (Element servletNode : servletNodes) {
            //获取servlet-name节点元素对象
            Element servletNameElt = (Element) servletNode.selectSingleNode("servlet-name");
            //获取servletNameElt节点元素的值
            String servletName = servletNameElt.getStringValue();

            //获取servlet-class节点那元素对象
            Element servletClassElt = (Element) servletNode.selectSingleNode("servlet-class");
            //获取servletClassElt节点元素对象的值
            String servletClassName = servletClassElt.getStringValue();

            //将servletName和servletClassName分别当作key和value

            servletInfoMap.put(servletName, servletClassName);
        }

        //servlet-mapping节点元素对象: web-app -> servlet-mapping
        List<Element> servletMappingNodes = document.selectNodes("/web-app/servlet-mapping");
        //创建一个servletMappingInfoMap集合，将servlet-name和url-pattern的值分别当作key和value
        HashMap<String, String> servletMappingInfoMap = new HashMap<String, String>();

        for (Element servletMappingNode : servletMappingNodes) {
            //获取servlet-name节点元素对象
            Element servletNameElt = (Element) servletMappingNode.selectSingleNode("servlet-name");
            //获取servletNameElt节点元素对象的值
            String servletName = servletNameElt.getStringValue();

            //获取url-pattern节点那元素对象
            Element urlPatternElt = (Element) servletMappingNode.selectSingleNode("url-pattern");
            //获取urlPatternElt节点元素对象的值
            String urlPattern = urlPatternElt.getStringValue();

            //将servletName和urlPattern分别当作key和value
            servletMappingInfoMap.put(servletName, urlPattern);
        }

        //获取servletInfoMap或者servletMappingInfoMap任何一个key值的集合
        Set<String> servletNames = servletInfoMap.keySet();
        //创建一个servletMap集合，将servletMappingInfoMap的value和servletInfoMap的value分别当作servletMap的key和value
        HashMap<String, String> servletMap = new HashMap<String, String>();
        //遍历servletNames
        for (String servletName : servletNames) {
            //获取 servletMappingInfoMap集合中的value: urlPattern
            String urlPattern = servletMappingInfoMap.get(servletName);
            //获取 servletnfoMap集合中的value: servletClass
            String servletClassName = servletInfoMap.get(servletName);

            //将urlPattern和servletClassName分别当作keyh和value存放到servletMap中
            servletMap.put(urlPattern, servletClassName);

        }


        return servletMap;
    }
}
