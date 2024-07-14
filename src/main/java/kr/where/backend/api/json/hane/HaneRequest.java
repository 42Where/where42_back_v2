package kr.where.backend.api.json.hane;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HaneRequest {
    private String login;

    public HaneRequest(final String login) {
        this.login = login;
    }
}
