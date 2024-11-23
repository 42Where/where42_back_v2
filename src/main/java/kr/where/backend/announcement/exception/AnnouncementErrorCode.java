package kr.where.backend.announcement.exception;

import kr.where.backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AnnouncementErrorCode implements ErrorCode {
    NO_ANNOUNCEMENT(1900, "작성된 공지가 존재하지 않습니다.");

    private final int errorCode;
    private final String errorMessage;
}
