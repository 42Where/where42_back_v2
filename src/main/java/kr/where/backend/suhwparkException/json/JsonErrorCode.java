package kr.where.backend.suhwparkException.json;

import kr.where.backend.suhwparkException.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JsonErrorCode implements ErrorCode {
    DESERIALIZE_FAIL(3009, "json 맵핑에 실패 했습니다.");

    private final int errorCode;
    private final String errorMessage;
}
