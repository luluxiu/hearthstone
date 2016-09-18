package com.tigercel.hearthstone.config;

import com.tigercel.hearthstone.Constants;
import com.tigercel.hearthstone.web.support.JsonResponseCode;
import com.tigercel.hearthstone.service.HFUserMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private HFUserMgrService hfUserMgrService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        PrintWriter pw = response.getWriter();
        //Cookie cookie = new Cookie(Constants.DEFAULT_SESSION_ID, request.getSession().getId());

        //cookie.setMaxAge(request.getSession().getMaxInactiveInterval());
        //cookie.setPath("/");
        //cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        //response.addCookie(cookie);
        //resetCookie(request, response);
        response.addHeader("Content-Type", "application/json");

        response.setStatus(HttpServletResponse.SC_OK);

        pw.print(hfUserMgrService.loginResponse(authentication, request.getSession()));
        pw.flush();
        clearAuthenticationAttributes(request);
    }




}

