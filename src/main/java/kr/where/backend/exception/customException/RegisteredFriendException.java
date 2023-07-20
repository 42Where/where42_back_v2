package kr.where.backend.exception.customException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import kr.where.backend.response.ResponseMsg;
import kr.where.backend.response.StatusCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 *     이미 등록된 친구를 추가로 등록하려고 하는 경우 발생
 *     error code: 409
 * </pre>
 * @see openproject.where42.groupFriend.GroupFriendApiController#createFriend(HttpServletRequest, HttpServletResponse, String, String, String) 친구 생성
 * @since 1.0
 * @author hyunjcho
 */
@Getter
@Slf4j
public class RegisteredFriendException extends RuntimeException {
    private int errorCode;

    public RegisteredFriendException() {
        super(ResponseMsg.REGISTERED_GROUP_FRIEND);
        this.errorCode = StatusCode.CONFLICT;
    }
}
