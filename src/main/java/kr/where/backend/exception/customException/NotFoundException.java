package kr.where.backend.exception.customException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import kr.where.backend.response.ResponseMsg;
import kr.where.backend.response.StatusCode;

/**
 * <pre>
 *     페이지를 찾을 수 없는 경우 발생
 *     error code: 400
 * </pre>
 * @since 2.0
 * @author hyunjcho
 */
@Getter
@Slf4j
public class NotFoundException extends RuntimeException {
    private int errorCode;

    public NotFoundException() {
        super(ResponseMsg.NOT_FOUND);
        this.errorCode = StatusCode.NOT_FOUND;
    }
}
