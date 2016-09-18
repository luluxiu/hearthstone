package com.tigercel.hearthstone.config;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigercel.hearthstone.web.support.JsonResponseCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by somebody on 2016/9/9.
 */
public class MyLoginAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private static final Log logger = LogFactory.getLog(MyLoginAuthenticationEntryPoint.class);

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public MyLoginAuthenticationEntryPoint(String url) {
        super(url);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        String redirectUrl = null;


        if ("application/json".equals(request.getHeader("Content-Type")) == true) {

            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().print(JsonResponseCode.NOT_VALID_REQUEST);
            response.getWriter().flush();
        }
        else {

            if (this.isUseForward()) {

                if (this.isForceHttps() && "http".equals(request.getScheme())) {
                    // First redirect the current request to HTTPS.
                    // When that request is received, the forward to the login page will be used.
                    redirectUrl = buildHttpsRedirectUrlForRequest(request);
                }

                if (redirectUrl == null) {
                    String loginForm = determineUrlToUseForThisRequest(request, response, authException);

                    if (logger.isDebugEnabled()) {
                        logger.debug("Server side forward to: " + loginForm);
                    }

                    RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);

                    dispatcher.forward(request, response);

                    return;
                }
            } else {
                // redirect to login page. Use https if forceHttps true

                redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);

            }

            redirectStrategy.sendRedirect(request, response, redirectUrl);
        }
    }

}