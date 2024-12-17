package kr.where.backend.auth.oauth2login.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieShop {
    public static void bakedCookie(
            final HttpServletResponse response,
            final String key,
            final int expiry,
            final String token,
            final boolean http) {
        final Cookie cookie = new Cookie(key, token);

        cookie.setDomain("dev.where42.kr");
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        cookie.setHttpOnly(http);
        cookie.setSecure(true);

        response.addCookie(cookie);
    }
}
