package kr.where.backend.auth.oauth2login.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieShop {
    public static void bakedCookie(
            final HttpServletResponse response,
            final String key,
            final int expiry,
            final String token) {
        final Cookie cookie = new Cookie(key, token);

        cookie.setDomain("where42.kr");
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        cookie.setSecure(true);

        response.addCookie(cookie);
    }
}
