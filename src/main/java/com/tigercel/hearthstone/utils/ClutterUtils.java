package com.tigercel.hearthstone.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tigercel.hearthstone.Constants;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by somebody on 2016/8/2.
 */
public class ClutterUtils {

    private static Logger logger = Logger.getLogger(ClutterUtils.class);

    public static JsonNode stringToJson(String content) {
        ObjectMapper    mapper;
        JsonNode        root;

        if(StringUtils.isEmpty(content) == true) {
            return null;
        }

        try {
            mapper = new ObjectMapper();
            root = mapper.readTree(content);
            return root;
        }
        catch (Exception e) {
            logger.error(content);
            return null;
        }
    }

    public static String MD5(String str) {
        MessageDigest   md5     = null;
        byte[]          digest;
        String          hashString ;


        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        digest = md5.digest(str.getBytes());
        hashString = new BigInteger(1, digest).toString(16);

        return hashString;
    }


    public static void resetCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            return;
        }

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(Constants.DEFAULT_SESSION_ID)) {
                cookie.setMaxAge(request.getSession().getMaxInactiveInterval());
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);
                return;
            }
        }
    }
}
