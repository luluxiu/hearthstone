package com.tigercel.hearthstone.config;

import com.tigercel.hearthstone.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Created by somebody on 2016/8/4.
 */
@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = Constants.DEFAULT_COOKIE_MAXAGE)
public class SessionCookieConfiguration {

    @Bean
    public CookieSerializer cookieSerializer() {

        //System.out.println("==============: cookieSerializer()");
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(Constants.DEFAULT_SESSION_ID);
        serializer.setCookiePath("/");
        serializer.setCookieMaxAge(Constants.DEFAULT_COOKIE_MAXAGE);
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");

        return serializer;
    }
}
