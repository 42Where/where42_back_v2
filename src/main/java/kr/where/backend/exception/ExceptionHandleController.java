package kr.where.backend.exception;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
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
}
