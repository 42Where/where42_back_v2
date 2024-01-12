package kr.where.backend.join.exception;

import kr.where.backend.exception.CustomException;

public class JoinException extends CustomException {
    public JoinException(final JoinErrorCode joinErrorCode) {
        super(joinErrorCode);
    }

    public static class DuplicatedJoinMember extends JoinException {
        public DuplicatedJoinMember() {
            super(JoinErrorCode.DUPLICATED_JOIN);
        }
    }
}
