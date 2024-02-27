package kr.where.backend.auth.filter;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.where.backend.jwt.exception.JwtException;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (final JwtException e) {
            sendRequest(response, HttpServletResponse.SC_UNAUTHORIZED, e.toString());
        } catch (final MemberException e) {
            sendRequest(response, HttpServletResponse.SC_NOT_FOUND, e.toString());
        }
    }

    private void sendRequest(
            final HttpServletResponse response,
            final int errorCode,
            final String error
    ) throws IOException {
        response.setStatus(errorCode);
        response.setCharacterEncoding(UTF8.getJavaName());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
