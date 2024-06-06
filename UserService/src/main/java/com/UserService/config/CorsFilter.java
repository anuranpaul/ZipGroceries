package com.UserService.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response    ;
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}

