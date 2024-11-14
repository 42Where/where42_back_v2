package kr.where.backend.aspect;

import kr.where.backend.auth.authUser.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestLogAspect {

//    private static final String POINTCUT = "@annotation(requestLogAspect)";
    private static final String POINTCUT = "execution(* kr.where.backend..*Service.*(..))";


    @AfterReturning(pointcut = POINTCUT,
            returning = "ret", argNames = "joinPoint, authUser, ret")
    public void requestSuccessLog(JoinPoint joinPoint, Object ret) {


        String responseString = (ret == null) ? "void" : ret.toString();
        sendLoggingMessage(joinPoint, responseString);
    }


    private void sendLoggingMessage(JoinPoint joinPoint, String responseString) {

        final ServletRequestAttributes attributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String ip = attributes.getRequest().getRemoteAddr();
        String requestUrl = attributes.getRequest().getRequestURI();
        String requestMethod = attributes.getRequest().getMethod();
        Integer userId = authUser.getIntraId();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        StringBuilder sb = new StringBuilder();

        sb.append("IP:").append(ip).append("#");
        sb.append("URL:").append(requestUrl).append("#");
        sb.append("METHOD:").append(requestMethod).append("#");
        sb.append("USERID:").append(userId).append("#");
        sb.append("EXCUTE_METHOD:").append(method.toString()).append("#");

        log.info(sb.toString());
    }

}
