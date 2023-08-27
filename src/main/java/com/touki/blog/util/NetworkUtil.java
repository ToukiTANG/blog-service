package com.touki.blog.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author Touki
 */
public abstract class NetworkUtil {
    public static void setJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(Objects.requireNonNull(JsonUtil.writeValueAsString(data)));
    }
}
