package kr.where.backend.member.exception;

import kr.where.backend.exception.CustomException;

public class MemberException extends CustomException {

	public MemberException(final MemberErrorCode memberErrorCode) {
		super(memberErrorCode);
	}

	public static class NoMemberException extends MemberException {
		public NoMemberException() {
			super(MemberErrorCode.NO_MEMBER);
		}
	}

	public static class DuplicatedMemberException extends MemberException {
		public DuplicatedMemberException() {
			super(MemberErrorCode.DUPLICATED_MEMBER);
		}
	}

	public static class NotFromSeoulCampus extends MemberException {
		public NotFromSeoulCampus() {
			super(MemberErrorCode.NOT_FROM_SEOUL_CADET);
		}
	}
}
