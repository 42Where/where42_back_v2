package kr.where.backend.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.where.backend.jwt.exception.JwtException;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private static final String ENCODING = "utf-8";
    private static final String CONTENT_TYPE ="application/json";
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            sendRequest(response, HttpServletResponse.SC_UNAUTHORIZED, e.toString());
        } catch (MemberException e) {
            sendRequest(response, HttpServletResponse.SC_NOT_FOUND, e.toString());
        }
    }

    private void sendRequest(
            final HttpServletResponse response,
            final int errorCode,
            final String error
    ) throws IOException {
        response.setStatus(errorCode);
        response.setCharacterEncoding(ENCODING);
        response.setContentType(CONTENT_TYPE);

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
