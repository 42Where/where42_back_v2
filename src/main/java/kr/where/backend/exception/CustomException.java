package kr.where.backend.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomException extends RuntimeException{
    private final int errorCode;
    private final String errorMessage;

    public <T extends Enum<T> & ErrorCode> CustomException(final T errorType) {
        errorCode = errorType.getErrorCode();
        errorMessage = errorType.getErrorMessage();
    }
}
