package kr.where.backend.group.exception;

import kr.where.backend.exception.CustomException;

public class GroupException extends CustomException {
    public GroupException(final GroupErrorCode groupErrorCode) {
        super(groupErrorCode);
    }

    public static class NoGroupException extends GroupException {
        public NoGroupException() {
            super(GroupErrorCode.NO_GROUP);
        }
    }

    public static class DuplicatedGroupNameException extends GroupException {
        public DuplicatedGroupNameException() {
            super(GroupErrorCode.DUPLICATED_GROUP_NAME);
        }
    }

    public static class CannotModifyGroupException extends GroupException {
        public CannotModifyGroupException() {super(GroupErrorCode.DUPLICATED_GROUP_NAME);}
    }
}
