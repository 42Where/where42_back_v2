package kr.where.backend.auth.filter;

import io.swagger.v3.oas.models.PathItem.HttpMethod;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {

        final String token = jwtService.extractToken(request).orElse(null);
        if (token != null) {
            final Authentication auth = jwtService.getAuthentication(request, token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        if (CorsUtils.isPreFlightRequest(request)) {
            response.addHeader("Access-Control-Allow-Origin", "https://test.where42.kr");
            return ;
        }
        filterChain.doFilter(request, response);
    }
}
