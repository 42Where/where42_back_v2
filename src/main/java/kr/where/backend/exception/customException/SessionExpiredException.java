package kr.where.backend.exception.customException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import kr.where.backend.response.ResponseMsg;
import kr.where.backend.response.StatusCode;

/**
 * <pre>
 *     세션이 만료된 경우 발생
 *     error code: 401
 * </pre>
 * @since 1.0
 * @author hyunjcho
 */
@Getter
@Slf4j
public class SessionExpiredException extends RuntimeException {
    private int errorCode;

    public SessionExpiredException() {
        super(ResponseMsg.NO_SESSION);
        this.errorCode = StatusCode.UNAUTHORIZED;
    }
}
