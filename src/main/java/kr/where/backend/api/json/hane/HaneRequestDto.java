package kr.where.backend.api.json.hane;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HaneRequestDto {
    private String login;

    public HaneRequestDto(final String login) {
        this.login = login;
    }
}
