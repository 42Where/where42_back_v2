package kr.where.backend.exception.customException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import kr.where.backend.response.ResponseMsg;
import kr.where.backend.response.StatusCode;

/**
 * <pre>
 *     외부 api 정보 매핑 실패 시 발생
 *     error code: 500
 * </pre>
 * @see openproject.where42.api.ApiService
 * @since 1.0
 * @author hyunjcho
 */
@Getter
@Slf4j
public class JsonDeserializeException extends RuntimeException {
    private int errorCode;

    public JsonDeserializeException() {
        super(ResponseMsg.JSON_DESERIALIZE_FAILED);
        this.errorCode = StatusCode.INTERNAL_SERVER_ERROR;
    }
}
