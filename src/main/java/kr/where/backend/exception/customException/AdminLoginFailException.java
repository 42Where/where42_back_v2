package kr.where.backend.exception.customException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import kr.where.backend.response.ResponseMsg;
import kr.where.backend.response.StatusCode;
/**
 * <pre>
 *     관리자 로그인 시 id - pwd 불일치 시 발생
 *     error code: 401
 * </pre>
 * @see AdminService#adminLogin(String, String)
 * @since 1.0
 * @author hyunjcho
 */
@Getter
@Slf4j
public class AdminLoginFailException extends RuntimeException{
	private int errorCode;

	public AdminLoginFailException() {
		super(ResponseMsg.ADMIN_FAIL);
		this.errorCode = StatusCode.UNAUTHORIZED;
	}
}
