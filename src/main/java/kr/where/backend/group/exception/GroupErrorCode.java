package kr.where.backend.group.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GroupErrorCode implements ErrorCode {
    NO_GROUP(1100, "그룹이 존재하지 않습니다."),
    DUPLICATED_GROUP_NAME(1101, "이미 존재하는 그룹 이름입니다."),
    CANNOT_MODIFY_GROUP(1102, "수정할 수 없는 그룹입니다.");

    private final int errorCode;
    private final String errorMessage;
}
