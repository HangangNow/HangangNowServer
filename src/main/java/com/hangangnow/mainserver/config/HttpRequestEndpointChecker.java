package com.hangangnow.mainserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;


public class HttpRequestEndpointChecker {

    private DispatcherServlet dispatcherServlet;

    public HttpRequestEndpointChecker(DispatcherServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
    }

    public boolean isEndpointExist(HttpServletRequest request) {

        for (HandlerMapping handlerMapping : dispatcherServlet.getHandlerMappings()) {
            try {
                HandlerExecutionChain foundHandler = handlerMapping.getHandler(request);
                if (foundHandler != null) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
