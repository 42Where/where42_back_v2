package kr.where.backend.group.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GroupErrorCode implements ErrorCode {
    NO_GROUP(2000, "그룹이 존재하지 않습니다."),
    DUPLICATED_GROUP_NAME(2001, "이미 존재하는 그룹 이름입니다.");

    private final int errorCode;
    private final String errorMessage;
}
