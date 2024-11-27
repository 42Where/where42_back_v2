package kr.where.backend.announcement.exception;

import kr.where.backend.exception.CustomException;

public class AnnouncementException extends CustomException {
    public AnnouncementException(final AnnouncementErrorCode errorCode) {
        super(errorCode);
    }

    public static class NoAnnouncementException extends AnnouncementException {
        public NoAnnouncementException() {
            super(AnnouncementErrorCode.NO_ANNOUNCEMENT);
        }
    }

    public static class InvalidArgumentException extends AnnouncementException {
        public InvalidArgumentException() {
            super(AnnouncementErrorCode.INVALID_ARGUMENTS);
        }
    }
}
