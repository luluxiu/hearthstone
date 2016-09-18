package com.tigercel.hearthstone.config;

import com.tigercel.hearthstone.service.HFUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by somebody on 2016/8/5.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Autowired
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Autowired
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Bean
    public HFUserService userService() {
        return new HFUserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .eraseCredentials(true)
                .userDetailsService(userService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity security) {
        security.ignoring().antMatchers(
                "/error/**",
                "/api/v1/account/signup",
                "/api/v1/account/smscode",
                "/api/v1/account/reset"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().authenticationEntryPoint(new MyLoginAuthenticationEntryPoint("/index"));


        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/index")
                .loginProcessingUrl("/api/v1/account/signin")
                .successHandler(ajaxAuthenticationSuccessHandler)
                .failureHandler(ajaxAuthenticationFailureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/api/v1/account/signout").logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                //.sessionFixation().changeSessionId()
                .maximumSessions(1).maxSessionsPreventsLogin(false)
                .and()
                .and().csrf().disable()
        ;
    }
}
