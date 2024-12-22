package com.example.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

public class ApiKeyAuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Get the request path
        String requestPath = httpRequest.getRequestURI();

        try {
            // Apply API key authentication only for the secure endpoint
            if (isSecureEndpoint(requestPath)) {
                Authentication authentication = ApiKeyAuthService.getAuthentication(httpRequest);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exp) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();
            writer.print(exp.getMessage());
            writer.flush();
            writer.close();
            return; // Stop filter chain for unauthorized requests
        }

        // Continue filter chain for all requests
        filterChain.doFilter(request, response);
    }

    /**
     * Check if the requested endpoint is the secure endpoint.
     *
     * @param requestPath The path of the incoming request.
     * @return true if the path matches /api/secure/spondEvents/**, false otherwise.
     */
    private boolean isSecureEndpoint(String requestPath) {
        return requestPath.startsWith("/api/secure/spondEvents/");
    }
}
