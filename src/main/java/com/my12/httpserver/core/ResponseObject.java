package com.my12.httpserver.core;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 * 负责封装响应参数对象
 * @author my12
 */
public class ResponseObject implements ServletResponse {
    private PrintWriter out;

    @Override
    public void setWriter(PrintWriter out){
        this.out = out;
    }

    @Override
    public PrintWriter getWriter(){
        return out;
    }
}
