package kr.where.backend.member.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {

	NO_MEMBER(1000, "존재하지 않는 맴버입니다."),
	DUPLICATED_MEMBER(1001, "이미 존재하는 맴버입니다.");

	private final int errorCode;
	private final String errorMessage;
}
