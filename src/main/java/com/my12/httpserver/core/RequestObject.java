package com.my12.httpserver.core;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责封装请求参数
 *
 * @author my12
 */
public class RequestObject implements ServletRequest {
    private Map<String, String[]> parameterMap = new HashMap<String, String[]>();


    public RequestObject(String requestURI) {
        /*
         * requestURI有以下几种情况
         * 1. /page/user?
         * 2. /page/user?username=zs
         * 3. /page/user?username=
         * 4. /page/user?username=zs&gender=1
         * 5. /page/user?username=zs&gender=1&interest=food
         * 6. /page/user?username=zs&gender=1&interest=food&interest=sleep
         * ...........
         */

        //判断requestURI中是否包含? 号： 是否包含参数
        if (requestURI.contains("?")) {
            //将requestURI通过? 号进行分割
            String[] uriAndData = requestURI.split("[?]");
            //再次判断requestURI是否包含参数
            if (uriAndData.length > 1) {
                //获取参数部分
                String data = uriAndData[1];

                //判断是否包含多个参数
                if (data.contains("&")) {//说明有多个参数
                    //{"username=zs","gender=1","interst=food","interst=sleep"}
                    String[] nameAndValues = data.split("&");
                    //开始遍历
                    for (String nameAndValue : nameAndValues) {
                        //通过 = 进行分割{"username","zs"}
                        String[] nameAndValueAttr = nameAndValue.split("=");
                        //判断key值是否在parameterMap集合中存在
                        //1.如果存在，说明该参数为多选框
                        //2.如果不存在：说明是普通标签
                        if (parameterMap.containsKey(nameAndValueAttr[0])) {
                            //将多选框的值取出来
                            String[] values = parameterMap.get(nameAndValueAttr[0]);
                            //定义一个新的数组，新数组的长度永远比values数组长度大1
                            String[] newValues = new String[values.length + 1];
                            System.arraycopy(values, 0, newValues, 0, values.length);
                            //判断该参数是否有值
                            if (nameAndValueAttr.length > 1) {
                                newValues[newValues.length - 1] = nameAndValueAttr[1];
                            } else {
                                newValues[newValues.length - 1] = "";

                            }

                            parameterMap.put(nameAndValueAttr[0], newValues);

                        } else {
                            //判断该参数是否有值
                            if (nameAndValueAttr.length > 1) {
                                parameterMap.put(nameAndValueAttr[0], new String[]{nameAndValueAttr[1]});
                            } else {
                                parameterMap.put(nameAndValueAttr[0], new String[]{""});

                            }
                        }
                    }

                } else {//单个参数
                    //data通过=号进行分割，判断参数是否有值
                    String[] nameAndValueAttr = data.split("=");

                    if (nameAndValueAttr.length > 1) {//有值
                        parameterMap.put(nameAndValueAttr[0], new String[]{nameAndValueAttr[1]});
                    } else {//无值

                        parameterMap.put(nameAndValueAttr[0], new String[]{""});

                    }

                }
            }
        }


    }

    /**
     * 获取普通标签参数的值
     * @param key 标签name属性的值
     * @return String 标签value值
     */
    @Override
    public String getParameterValue(String key){
        String[] value = parameterMap.get(key);

        return (value !=null &&value.length != 0) ? value[0]:null;
    }

    /**
     * 获取多选框的值
     * @param key 标签name属性的值
     * @return String[] 多选框的值
     */
    @Override
    public String[] getParameterValues(String key){
       return parameterMap.get(key);

    }
}
