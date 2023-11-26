package kr.where.backend.group.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GroupMemberErrorCode implements ErrorCode {
    DUPLICATED_GROUP_MEMBER(2002, "이미 등록된 맴버입니다.");

    private final int errorCode;
    private final String errorMessage;
}
