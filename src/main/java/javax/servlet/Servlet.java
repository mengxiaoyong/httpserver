package javax.servlet;

/**
 * 由sun公司制定的Servlet接口规范，该接口由web服务器开发人员来调用，由webApp开发人员来实现
 * @author sun
 */
public interface Servlet {

    /**
     * 处理业务的核心方法
     * @param request
     * @param response
     */
    void service(ServletRequest request, ServletResponse response);
}
