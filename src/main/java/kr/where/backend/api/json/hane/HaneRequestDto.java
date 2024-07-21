package kr.where.backend.api.json.hane;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HaneRequestDto {
    private String login;

    public HaneRequestDto(final String login) {
        this.login = login;
    }
}
