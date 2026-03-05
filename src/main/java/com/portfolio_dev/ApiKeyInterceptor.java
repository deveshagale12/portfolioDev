package com.portfolio_dev;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    // This pulls the value from application.properties
    @Value("${portfolio.api.key}")
    private String apiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Skip check for OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String requestApiKey = request.getHeader("X-API-KEY");

        if (apiKey.equals(requestApiKey)) {
            return true;
        }

        // If unauthorized, return 401 and a message
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Invalid or missing API Key\"}");
        return false;
    }
}