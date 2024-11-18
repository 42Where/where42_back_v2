package kr.where.backend.aspect;

import kr.where.backend.auth.authUser.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Objects;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestLogAspect {
    private static final String POINTCUT = "@annotation(kr.where.backend.aspect.RequestLogging)";
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @AfterReturning(value = "@annotation(requestLogging)", argNames = "joinPoint, requestLogging")
    public void requestSuccessLog(JoinPoint joinPoint, RequestLogging requestLogging) {
        String responseString = parseMessage(joinPoint);
        String logString = sendLoggingMessage(joinPoint, responseString);
        printLog(requestLogging.level(), logString);
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "exception", argNames = "joinPoint, exception")
    public void requestFailureLog(JoinPoint joinPoint, Exception exception) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(exception.getClass().getName());
        errorMessage.append(exception.getMessage());
        sendLoggingMessage(joinPoint, errorMessage.toString());
    }

    private void printLog(LogLevel level, String logString) {
        switch (level) {
            case TRACE -> log.trace(logString);
            case DEBUG -> log.debug(logString);
            case WARN -> log.warn(logString);
            case ERROR -> log.error(logString);
            default -> log.info(logString);
        }
    }

    private String parseMessage(JoinPoint joinPoint) {
        String[] classPath = joinPoint.getSignature().getDeclaringType().getName().split("\\.");
        String callerClass = classPath[classPath.length - 1];
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = discoverer.getParameterNames(method);

        StringBuilder sb = new StringBuilder();
        sb.append(callerClass).append(" - ");
        sb.append("Called ").append(methodName).append(" ");
        if (Objects.nonNull(parameterNames)) {
            for (int i = 0; i < args.length; i++) {
                sb.append(parameterNames[i]).append(": ").append(args[i]).append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

    private String sendLoggingMessage(JoinPoint joinPoint, String responseString) {

        final ServletRequestAttributes attributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String ip = attributes.getRequest().getRemoteAddr();
        final String requestUrl = attributes.getRequest().getRequestURI();
        final String requestMethod = attributes.getRequest().getMethod();
        final Integer userId = authUser.getIntraId();
        final String method = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        StringBuilder sb = new StringBuilder();

        sb.append("[IP:").append(ip).append("] | ");
        sb.append("[URL:").append(requestUrl).append("] | ");
        sb.append("[METHOD:").append(requestMethod).append("] | ");
        sb.append("[USERID:").append(userId).append("] | ");
        sb.append("[EXCUTE_METHOD:").append(method).append("] | ");
        sb.append("[MSG: ").append(responseString).append("]");

        return sb.toString();
    }
}
