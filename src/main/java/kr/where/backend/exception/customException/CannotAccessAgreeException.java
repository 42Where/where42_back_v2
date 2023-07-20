package kr.where.backend.exception.customException;

import kr.where.backend.utils.response.ResponseMsg;
import kr.where.backend.utils.response.StatusCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * <pre>
 *     이미 가입된 멤버가 개인정보 제공 동의 페이지 임의 접근 시 발생
 *     error code: 409
 * </pre>
 * @see openproject.where42.util.LoginApiController#checkAgree(HttpServletRequest, HttpServletResponse, String) 동의 페이지 접근 가능 여부 조회
 * @since 1.0
 * @author hyunjcho
 */
@Getter
@Slf4j
public class CannotAccessAgreeException extends RuntimeException {
    private int errorCode;

    public CannotAccessAgreeException() {
        super(ResponseMsg.CANNOT_ACCESS_AGREE);
        this.errorCode = StatusCode.CONFLICT;
    }
}
