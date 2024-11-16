package kr.where.backend.aspect;

import kr.where.backend.auth.authUser.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class ContextUtil {
    private final ServletRequestAttributes attributes;
    private final AuthUser authUser;

    public ContextUtil() {
        this.attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        this.authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getRequestIp() {
        return Optional.ofNullable(attributes)
                .map(attributes -> attributes.getRequest().getRemoteAddr())
                .orElse("unknown");
    }

    public String getRequestURL() {
        return Optional.ofNullable(attributes)
                .map(attributes -> attributes.getRequest().getRequestURL().toString())
                .orElse("unknown");
    }

    public String getRequestMethod() {
        return Optional.ofNullable(attributes)
                .map(attributes -> attributes.getRequest().getMethod())
                .orElse("unknown");
    }

    public Integer getUserIntraId() {
        return Optional.ofNullable(authUser)
                .map(AuthUser::getIntraId)
                .orElse(0);
    }

}
