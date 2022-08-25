package com.hangangnow.mainserver.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hangangnow.mainserver.config.security.HttpRequestEndpointChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.DispatcherServlet;

@RequiredArgsConstructor
@Configuration
public class BeanConfig {

    private final DispatcherServlet dispatcherServlet;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpRequestEndpointChecker endpointChecker() {
        return new HttpRequestEndpointChecker(dispatcherServlet);
    }

    @Bean
    public static ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new SimpleModule())
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
