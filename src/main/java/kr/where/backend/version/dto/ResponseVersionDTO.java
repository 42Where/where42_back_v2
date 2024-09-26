package kr.where.backend.version.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseVersionDTO {
    private String Version;

    @Builder
    public ResponseVersionDTO(final String latestVersion) {
        this.Version = latestVersion;
    }
}