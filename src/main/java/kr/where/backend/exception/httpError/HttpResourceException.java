package kr.where.backend.exception.httpError;

import kr.where.backend.exception.ErrorCode;

public class HttpResourceException {
    public static String of(final ErrorCode errorCode) {
        return "CustomException(errorCode=" + errorCode.getErrorCode()  + ", " + "errorMessage=" + errorCode.getErrorMessage()+")";
    }
}
