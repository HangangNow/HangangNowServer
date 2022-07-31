package com.hangangnow.mainserver.config.jwt;

import com.hangangnow.mainserver.config.security.HttpRequestEndpointChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private HttpRequestEndpointChecker endpointChecker;

    public JwtAuthenticationEntryPoint(HttpRequestEndpointChecker endpointChecker) {
        this.endpointChecker = endpointChecker;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        

        if(!endpointChecker.isEndpointExist(request)){
            log.error("No handler found for " + request.getMethod() + " " + request.getRequestURI());
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page NOT FOUND");
        }

        else{
            // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}