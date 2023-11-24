package kr.where.backend.search.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchErrorCode implements ErrorCode {
    INVALID(3010, "유효하지 않은 검색 입력 값입니다.");

    private final int errorCode;
    private final String errorMessage;
}
