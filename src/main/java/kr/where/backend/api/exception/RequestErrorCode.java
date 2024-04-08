package kr.where.backend.api.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestErrorCode implements ErrorCode {
    UNAUTHORIZED(3000, "Unauthorized 권한이 없습니다."),
    TOO_MANY_REQUEST(3001, "42API 요청 횟수를 초과하였습니다."),
    SERVER_ERROR(3002, "외부 API 서버 에러입니다."),
    BAD_REQUEST(3003, "잘못된 요청입니다");

    private final int errorCode;
    private final String errorMessage;
}
