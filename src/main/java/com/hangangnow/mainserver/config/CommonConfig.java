package com.hangangnow.mainserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.DispatcherServlet;

@RequiredArgsConstructor
@Configuration
public class CommonConfig {

    private final DispatcherServlet dispatcherServlet;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpRequestEndpointChecker endpointChecker() {
        return new HttpRequestEndpointChecker(dispatcherServlet);
    }
}
