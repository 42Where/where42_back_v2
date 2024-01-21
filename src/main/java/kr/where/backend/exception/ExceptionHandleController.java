package kr.where.backend.exception;

import jakarta.servlet.http.HttpServletResponse;
import kr.where.backend.auth.authUser.exception.AuthUserException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
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

    @ExceptionHandler(AuthUserException.class)
    public ResponseEntity<String> handleAuthUserException(final CustomException e) {
        log.info(e.toString());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParameterException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(HttpResourceErrorCode.NO_PARAMETERS));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNoRequestBodyException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(HttpResourceErrorCode.NO_REQUEST_BODY));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleUnsupportedMethodException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HttpResourceException.of(HttpResourceErrorCode.NO_SUPPORTED_METHOD));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Integer> handleNoResourceException(final HttpServletResponse response) {
        int errorCode = response.getStatus();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorCode);
    }
}
