package kr.where.backend.search.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchErrorCode implements ErrorCode {
    INVALID_CONTEXT(1300, "유효하지 않은 검색 입력 값입니다."),
    INVALID_LENGTH(1301, "2자 이상 입력해주시길 바랍니다.");

    private final int errorCode;
    private final String errorMessage;
}
