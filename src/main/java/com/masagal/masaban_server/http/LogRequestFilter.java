package com.masagal.masaban_server.http;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LogRequestFilter extends OncePerRequestFilter {
    private Logger logger = LogManager.getLogger();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("{} {} begun", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
        logger.info("request complete {} {}", request.getMethod(), request.getRequestURI());
    }
}
