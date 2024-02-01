package kr.where.backend.exception.httpError;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpResourceErrorCode implements ErrorCode {

    NO_PARAMETERS(1700, "파라미터가 없는 Api 요청입니다."),
    NO_REQUEST_BODY(1701, "Request body가 없는 Api 요청입니다."),
    NO_SUPPORTED_METHOD(1702, "지원하지 않는 Http Method 요청입니다."),
    NOT_METHOD_VALID_ARGUMENT(1703, "Method에 맞는 arguments가 없습니다");

    private final int errorCode;
    private final String errorMessage;
}
