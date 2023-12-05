package kr.where.backend.exception.customExceptionBysuhwpark;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse form(CustomException customException) {
        return new ErrorResponse(customException.getCode(), customException.getMessage());
    }
}
