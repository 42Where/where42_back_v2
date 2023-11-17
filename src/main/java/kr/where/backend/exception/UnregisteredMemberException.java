package kr.where.backend.exception.customException;

import kr.where.backend.member.DTO.Seoul42;
import kr.where.backend.utils.response.ResponseMsg;
import kr.where.backend.utils.response.StatusCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *     아직 가입되지 않은 멤버의 경우 발생
 *     error code: 401
 *     data: seoul42 정보를 front에 같이 반환하여 멤버 가입 시 이름 등 정보 활용
 * </pre>
 * @see openproject.where42.member.MemberApiController
 * @since 1.0
 * @author hyunjcho
 */
@Getter
@Slf4j
public class UnregisteredMemberException extends RuntimeException {
    private int errorCode;
    private Seoul42 seoul42;

    public UnregisteredMemberException() {
        super(ResponseMsg.UNREGISTERED);
        this.errorCode = StatusCode.UNAUTHORIZED;
    }

    public UnregisteredMemberException(Seoul42 seoul42) {
        super(ResponseMsg.UNREGISTERED);
        this.errorCode = StatusCode.UNAUTHORIZED;
        this.seoul42 = seoul42;
    }
}
