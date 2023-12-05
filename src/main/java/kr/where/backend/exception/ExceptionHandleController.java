package kr.where.backend.exception;

import kr.where.backend.group.exception.GroupException;
import kr.where.backend.group.exception.GroupMemberException;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.exception.json.JsonException;
import kr.where.backend.exception.request.RequestException;
import kr.where.backend.exception.token.TokenException;
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

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenException(final CustomException e) {
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

}
