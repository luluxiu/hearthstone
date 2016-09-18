package com.tigercel.hearthstone.aop;

import com.tigercel.hearthstone.Constants;
import com.tigercel.hearthstone.utils.ClutterUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Created by somebody on 2016/8/3.
 */
@Aspect
@Component
public class CookieAspect {
    private Logger logger = Logger.getLogger(getClass());

    @Pointcut("execution(public * com.tigercel.hearthstone.web.*.*(..))")
    public void cookiePointCut() {}


    @AfterReturning(pointcut = "cookiePointCut()")
    public void doAfterReturning() throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        ClutterUtils.resetCookie(attributes.getRequest(), attributes.getResponse());


/*
        Cookie cookie = new Cookie(Constants.DEFAULT_SESSION_ID, request.getSession().getId());
        cookie.setMaxAge(request.getSession().getMaxInactiveInterval());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        logger.warn("============ session : " + request.getSession().getMaxInactiveInterval());
        */

    }
}
