package com.example.product.security;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRemoteAuthFilter implements Filter {

    private final RemoteAuthService authService;

    public JwtRemoteAuthFilter(RemoteAuthService authService) {
        this.authService = authService;
    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
       
        String authHeader = ((HttpServletRequest) request).getHeader("Authorization");

        if (authHeader == null || !authService.isTokenValid(authHeader.substring(7))) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o faltante");
            return;
        }

        chain.doFilter(request, response);
		
	}
}
