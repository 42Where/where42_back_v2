package kr.where.backend.auth.authUser;

import kr.where.backend.auth.authUser.exception.AuthUserException;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

@Setter
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
        return intraId;
    }

    public String getIntraName() {
        return intraName;
    }

    public Long getDefaultGroupId() {
        if (defaultGroupId != null) {
            return defaultGroupId;
        }
        throw new AuthUserException.ForbiddenUserException();
    }

    public void setDefaultGroupId(final Long groupId) {
        this.defaultGroupId = groupId;
    }
    public static AuthUser of() {
        return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
