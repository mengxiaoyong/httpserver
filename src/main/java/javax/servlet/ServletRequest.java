package javax.servlet;

/**
 *负责封装请求参数对象
 * @author my12
 */
public interface ServletRequest {

    /**
     * 获取单个参数的值
     * @param key
     * @return
     */
    String getParameterValue(String key);

    /**
     * 获取多选框的值
     * @param key
     * @return
     */
    String[] getParameterValues(String key);


}
