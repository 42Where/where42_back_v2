package kr.where.backend.auth.authUserInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@Setter
public class AuthUserInfo {
    private Integer intraId;
    private String intraName;
    private Long defaultGroupId;

    @Builder
    public AuthUserInfo(final Integer intraId, final String intraName, final Long defaultGroupId) {
        this.intraId = intraId;
        this.intraName = intraName;
        this.defaultGroupId = defaultGroupId;
    }

    public static AuthUserInfo of() {
        return (AuthUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
