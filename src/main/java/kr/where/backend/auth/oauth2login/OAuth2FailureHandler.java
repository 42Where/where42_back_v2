package kr.where.backend.auth.oauth2login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.rmi.RemoteException;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
//        final String targetUri = UriComponentsBuilder
//                .fromUriString("http://localhost:3000/")
//                .build()
//                .toUriString();
//
//        getRedirectStrategy().sendRedirect(request, response, targetUri);
        response.sendRedirect("http://13.209.149.15/login-fail");
    }
}
