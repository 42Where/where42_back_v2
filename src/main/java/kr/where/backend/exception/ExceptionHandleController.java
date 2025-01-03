package kr.where.backend.exception;

import kr.where.backend.admin.exception.AdminException;
import kr.where.backend.announcement.exception.AnnouncementErrorCode;
import kr.where.backend.announcement.exception.AnnouncementException;
import kr.where.backend.announcement.exception.AnnouncementException.NoAnnouncementException;
import kr.where.backend.aspect.LogLevel;
import kr.where.backend.aspect.RequestLogging;
import kr.where.backend.auth.authUser.exception.AuthUserException;
import kr.where.backend.cluster.exception.ClusterErrorCode;
import kr.where.backend.cluster.exception.ClusterException.InvalidPathVariable;
import kr.where.backend.exception.httpError.HttpResourceErrorCode;
import kr.where.backend.exception.httpError.HttpResourceException;
import kr.where.backend.group.exception.GroupException;
import kr.where.backend.group.exception.GroupMemberException;
import kr.where.backend.join.exception.JoinException;
import kr.where.backend.jwt.exception.JwtException;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.api.exception.JsonException;
import kr.where.backend.api.exception.RequestException;
import kr.where.backend.oauthtoken.exception.OAuthTokenException;
import kr.where.backend.search.exception.SearchException;
import kr.where.backend.version.exception.VersionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequestLogging(level = LogLevel.WARN)
public class ExceptionHandleController {

    @ExceptionHandler({MemberException.NoMemberException.class, GroupException.NoGroupException.class})
    public ResponseEntity<String> handleNoResultException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.toString());
    }

    @ExceptionHandler({MemberException.DuplicatedMemberException.class, GroupException.DuplicatedGroupNameException.class,
            GroupMemberException.class})
    public ResponseEntity<String> handleDuplicatedException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
    }

    @ExceptionHandler(GroupException.CannotModifyGroupException.class)
    public ResponseEntity<String> handleCannotModifiedException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
    }

    @ExceptionHandler(OAuthTokenException.class)
    public ResponseEntity<String> handleOAuthTokenException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.toString());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleJwtTokenException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.toString());
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<String> handleBadRequestException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
    }

    @ExceptionHandler(JsonException.class)
    public ResponseEntity<String> handleJsonException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
    }

    @ExceptionHandler(JoinException.class)
    public ResponseEntity<String> handleJoinException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
    }

    @ExceptionHandler(SearchException.class)
    public ResponseEntity<String> handleSearchException(final  CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
    }

    @ExceptionHandler(AuthUserException.class)
    public ResponseEntity<String> handleAuthUserException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParameterException() {
        log.error(HttpResourceErrorCode.NO_PARAMETERS.getErrorMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(HttpResourceErrorCode.NO_PARAMETERS));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNoRequestBodyException() {
        log.error(HttpResourceErrorCode.NO_REQUEST_BODY.getErrorMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(HttpResourceErrorCode.NO_REQUEST_BODY));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleUnsupportedMethodException() {
        log.error(HttpResourceErrorCode.NO_SUPPORTED_METHOD.getErrorMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(HttpResourceErrorCode.NO_SUPPORTED_METHOD));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException() {
        log.error(HttpResourceErrorCode.NOT_METHOD_VALID_ARGUMENT.getErrorMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(HttpResourceErrorCode.NOT_METHOD_VALID_ARGUMENT));
    }

    @ExceptionHandler(RuntimeException.class)
    @RequestLogging(level = LogLevel.ERROR)
    public ResponseEntity<String> handleNoResourceException(final RuntimeException e) {
        log.error(HttpResourceErrorCode.INTERNAL_SERVER_ERROR.getErrorMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 요청하세요.");
    }

    @ExceptionHandler({VersionException.NotAllowedOsException.class,
            VersionException.InvalidVersionFormatException.class,
            AnnouncementException.InvalidArgumentException.class})
    public ResponseEntity<String> handleInvalidRequestArgument(final CustomException e) {
        log.error(e.toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.toString());
    }

    @ExceptionHandler(VersionException.RequireVersionUpgradeException.class)
    public ResponseEntity<String> handleRequireVersionUpgrade(final CustomException e) {
        log.error(e.toString());

        return ResponseEntity
                .status(HttpStatus.UPGRADE_REQUIRED)
                .body(e.toString());
    }

    @ExceptionHandler(AdminException.permissionDeniedException.class)
    public ResponseEntity<String> handlePermissionDenied(final CustomException e) {
        log.error(e.toString());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.toString());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDtoNotValidException() {
        log.error(HttpResourceErrorCode.INVALID_REQUEST_BODY.getErrorMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(HttpResourceErrorCode.INVALID_REQUEST_BODY));
    }

    @ExceptionHandler(NoAnnouncementException.class)
    public ResponseEntity<String> handleNoAnnouncementException() {
        log.error(AnnouncementErrorCode.NO_ANNOUNCEMENTS.getErrorMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(AnnouncementErrorCode.NO_ANNOUNCEMENTS));
    }

    @ExceptionHandler(InvalidPathVariable.class)
    public ResponseEntity<String> handleInvalidPathVariable() {
        log.error(ClusterErrorCode.INVALID_PATH_VARIABLE.getErrorMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(ClusterErrorCode.INVALID_PATH_VARIABLE));
    }
}
