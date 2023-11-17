package kr.where.backend.exception.customException;

import kr.where.backend.utils.response.ResponseMsg;
import kr.where.backend.utils.response.StatusCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


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
