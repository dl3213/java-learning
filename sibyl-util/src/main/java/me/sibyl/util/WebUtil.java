package me.sibyl.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname WebUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 16:07
 */

public class WebUtil {

    public static void renderString(HttpServletResponse response, String str) {
        PrintWriter out = null;
        try {
            response.setStatus(200);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            out = response.getWriter();
            out.print(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
