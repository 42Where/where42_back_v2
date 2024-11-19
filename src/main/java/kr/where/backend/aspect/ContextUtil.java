package kr.where.backend.aspect;

import kr.where.backend.auth.authUser.AuthUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextUtil {
    public static final String SCHEDULING = "ScheduleTask";
    public static final int DEFAULT_USER_ID = 0;

    public static ServletRequestAttributes getAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static AuthUser getAuthUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .filter(AuthUser.class::isInstance)
                .map(AuthUser.class::cast)
                .orElse(null);
    }

    public static String getRequestIp() {
        return Optional.ofNullable(getAttributes())
                .map(attributes -> attributes.getRequest().getRemoteAddr())
                .orElse(SCHEDULING);
    }

    public static String getRequestURL() {
        return Optional.ofNullable(getAttributes())
                .map(attributes -> attributes.getRequest().getRequestURL().toString())
                .orElse(SCHEDULING);
    }

    public static String getRequestMethod() {
        return Optional.ofNullable(getAttributes())
                .map(attributes -> attributes.getRequest().getMethod())
                .orElse(SCHEDULING);
    }

    public static Integer getUserIntraId() {
        return Optional.ofNullable(getAuthUser())
                .map(AuthUser::getIntraId)
                .orElse(DEFAULT_USER_ID);
    }
}
