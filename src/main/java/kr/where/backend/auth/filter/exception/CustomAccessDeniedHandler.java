package kr.where.backend.auth.filter.exception;

import static com.fasterxml.jackson.core.JsonEncoding.UTF8;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException {
        //필요한 권한이 없이 접근하려 할때 403
        sendErrorResponse(response);
//        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    private void sendErrorResponse(final HttpServletResponse response) throws IOException{
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(UTF8.getJavaName());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.getWriter().write(objectMapper.writeValueAsString("접근 권한을 확인하세요."));
    }
}
