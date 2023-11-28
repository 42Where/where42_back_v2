package kr.where.backend.jwt.dto;

import lombok.Getter;

@Getter
public class ReIssue {
    private Long intraId;
    private String refreshToken;
}
