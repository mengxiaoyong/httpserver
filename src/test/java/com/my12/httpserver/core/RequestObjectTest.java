package com.my12.httpserver.core;


import org.junit.Test;


/**
 * @author my12
 */
public class RequestObjectTest {

    @Test
    public void getParameterValue() {
/*        RequestObject a = new RequestObject("/page/user/save?");

        System.out.println(a.getParameterValue("username"));

        RequestObject b = new RequestObject("/page/user/save?username=");

        System.out.println(b.getParameterValue("username"));*/

        RequestObject c = new RequestObject("/page/user/save?username=zs&gender=1&interest=food");

        System.out.println(c.getParameterValue("username"));
        System.out.println(c.getParameterValue("gender"));
        System.out.println(c.getParameterValue("interest"));


    }

    @Test
    public void getParameterValues() {
        RequestObject c = new RequestObject("/page/user/save?username=zs&gender=1&interest=food&interest=music");
        String[] interests = c.getParameterValues("interest");
        for (String interest : interests) {

            System.out.println(interest);
        }

    }
}
