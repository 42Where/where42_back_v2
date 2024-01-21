package kr.where.backend.auth.authUser;

import kr.where.backend.auth.authUser.exception.AuthUserException;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public class AuthUser {
    private Integer intraId;
    private String intraName;
    private Long defaultGroupId;

    @Builder
    public AuthUser(final Integer intraId, final String intraName, final Long defaultGroupId) {
        this.intraId = intraId;
        this.intraName = intraName;
        this.defaultGroupId = defaultGroupId;
    }

    public Integer getIntraId() {
        return this.intraId;
    }

    public String getIntraName() {
        return this.intraName;
    }

    public Long getDefaultGroupId() {
        if (this.defaultGroupId != null) {
            return this.defaultGroupId;
        }
        throw new AuthUserException.ForbiddenUserException();
    }

    public static AuthUser of() {
        return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
