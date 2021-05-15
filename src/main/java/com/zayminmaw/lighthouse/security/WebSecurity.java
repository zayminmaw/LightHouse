package com.zayminmaw.lighthouse.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST,"/api/buyer/signup").permitAll()
                .antMatchers(HttpMethod.POST,"/api/buyer/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/seller/signup").permitAll()
                .antMatchers(HttpMethod.POST,"/api/seller/login").permitAll()
                .anyRequest().authenticated().and().apply(new JwtConfigurer(jwtTokenProvider));
        http.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.addAllowedOrigin("*");
                config.setAllowCredentials(true);
                return config;
            }
        });
        http.headers()
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin","*"))
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods","GET,HEAD,OPTIONS,POST,PUT"))
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Credentials", "*"))
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers","Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization"));
    }

}