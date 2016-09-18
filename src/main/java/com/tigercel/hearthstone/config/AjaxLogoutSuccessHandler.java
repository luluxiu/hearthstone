package com.tigercel.hearthstone.config;

import com.tigercel.hearthstone.web.support.JsonResponseCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by somebody on 2016/8/11.
 */
@Component
public class AjaxLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        //super.handle(request, response, authentication);

        PrintWriter pw = response.getWriter();

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Type", "application/json");
        pw.print(JsonResponseCode.SIMPLE_MSG);
        pw.flush();
    }

}
