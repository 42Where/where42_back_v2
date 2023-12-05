package kr.where.backend.api.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestErrorCode implements ErrorCode {
    TOO_MANY_REQUEST(3005, "42API 요청 횟수를 초과하였습니다."),
    HANE_SERVICE(3006, "HANE-API 요청 실패"),
    WHITE_LABEL_PAGE(3007, "잘못된 접근입니다"),
    BAD_REQUEST(3008, "잘못된 요청입니다");

    private final int errorCode;
    private final String errorMessage;
}
