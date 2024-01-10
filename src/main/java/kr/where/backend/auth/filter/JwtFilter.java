package kr.where.backend.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.exception.CustomException;
import kr.where.backend.jwt.JwtService;
import kr.where.backend.jwt.exception.JwtException;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

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

        final String token = extractToken(request).orElse(null);
        if (token != null) {
            try {
                final Authentication auth = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException e) {
                sendRequest(response, HttpServletResponse.SC_UNAUTHORIZED, e.toString());
                return ;
            } catch (MemberException e) {
                sendRequest(response, HttpServletResponse.SC_NOT_FOUND, e.toString());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(final HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isEmpty(authorization)) {
            return Optional.empty();
        }
        return getToken(authorization.split(" "));
    }

    private Optional<String> getToken(final String[] token) {
        if (token.length != 2 || !token[0].equals("Bearer")) {
            return Optional.empty();
        }
        return Optional.ofNullable(token[1]);
    }

    private void sendRequest(
            final HttpServletResponse response,
            final int errorCode,
            final String error
    ) throws IOException{
        response.setStatus(errorCode);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(error);

        response.getWriter().write(result);
    }
}
