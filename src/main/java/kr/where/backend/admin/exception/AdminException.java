package kr.where.backend.admin.exception;

import kr.where.backend.exception.CustomException;

public class AdminException extends CustomException {
  public AdminException(final AdminErrorCode errorCode) {super(errorCode);}

  public static class permissionDeniedException extends AdminException {
    public permissionDeniedException() {super(AdminErrorCode.PERMISSION_DENIED);}
  }
}
