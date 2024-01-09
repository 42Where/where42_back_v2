package kr.where.backend.auth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver exceptionResolver;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") final HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {
        if (request.getAttribute("jwtException") != null) {
            exceptionResolver.resolveException(request, response, null, (Exception) request.getAttribute("jwtException"));
            return ;
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
