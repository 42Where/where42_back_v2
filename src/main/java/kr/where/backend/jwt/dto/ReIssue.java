package kr.where.backend.jwt.dto;

import lombok.Getter;

@Getter
public class ReIssue {
    private Integer intraId;
    private String refreshToken;
}
