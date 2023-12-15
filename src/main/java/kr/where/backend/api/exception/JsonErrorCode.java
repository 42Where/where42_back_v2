package kr.where.backend.api.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JsonErrorCode implements ErrorCode {
    DESERIALIZE_FAIL(3009, "json 맵핑에 실패 했습니다.");

    private final int errorCode;
    private final String errorMessage;
}
