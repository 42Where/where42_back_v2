package kr.where.backend.group.exception;

import kr.where.backend.exception.CustomException;

public class GroupMemberException extends CustomException {
    public GroupMemberException(final GroupMemberErrorCode groupMemberErrorCode) {
        super(groupMemberErrorCode);
    }

    public static class DuplicatedGroupMemberException extends GroupMemberException{
        public DuplicatedGroupMemberException() {
            super(GroupMemberErrorCode.DUPLICATED_GROUP_MEMBER);
        }
    }
}
