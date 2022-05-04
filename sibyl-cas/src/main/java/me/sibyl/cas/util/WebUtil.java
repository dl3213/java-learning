package me.sibyl.cas.util;

import javax.servlet.http.HttpServletResponse;

/**
 * @Classname WebUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 16:07
 */

public class WebUtil {
    public static String renderString(HttpServletResponse response,String str){
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
