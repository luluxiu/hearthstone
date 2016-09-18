package com.tigercel.hearthstone.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class WebLogAspect {

    private Logger logger = Logger.getLogger(getClass());

    @Pointcut("execution(public * com.tigercel.hearthstone.web.*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logger.warn("\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -" +
                " - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        logger.warn("URL          : " + request.getRequestURL().toString());
        logger.warn("HTTP_METHOD  : " + request.getMethod());
        logger.warn("IP           : " + request.getRemoteAddr());
        logger.warn("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.warn("ARGS         : " + Arrays.toString(joinPoint.getArgs()));

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.warn("RESPONSE     : " + ret);
    }

}