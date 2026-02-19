package com.example.springangular.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CspNonceFilter extends OncePerRequestFilter {

    public static final String NONCE_ATTR = "cspNonce";
    public static final String NONCE_HEADER = "X-CSP-Nonce";

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String sessionNonce = (String) session.getAttribute(NONCE_ATTR);
        if (sessionNonce == null || sessionNonce.isBlank()) {
            sessionNonce = generateNonce();
            session.setAttribute(NONCE_ATTR, sessionNonce);
        }

        request.setAttribute(NONCE_ATTR, sessionNonce);
        response.setHeader(NONCE_HEADER, sessionNonce);
        response.setHeader(
                "Content-Security-Policy",
                "default-src 'self'; " +
                        "script-src 'self' 'nonce-" + sessionNonce + "'; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data:; " +
                        "font-src 'self'; " +
                        "connect-src 'self'; " +
                        "base-uri 'self'; " +
                        "frame-ancestors 'none'"
        );

        if (isApiRequest(request) && isUnsafeMethod(request.getMethod())) {
            String requestNonce = request.getHeader(NONCE_HEADER);
            if (!sessionNonce.equals(requestNonce)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Invalid CSP nonce");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/") || request.getRequestURI().equals("/api");
    }

    private boolean isUnsafeMethod(String method) {
        return HttpMethod.POST.matches(method)
                || HttpMethod.PUT.matches(method)
                || HttpMethod.PATCH.matches(method)
                || HttpMethod.DELETE.matches(method);
    }

    private String generateNonce() {
        byte[] nonce = new byte[24];
        secureRandom.nextBytes(nonce);
        return Base64.getEncoder().encodeToString(nonce);
    }
}
