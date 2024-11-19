package kr.where.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Component
public class LogUtil {
    private final ParameterNameDiscoverer discoverer;

    public LogUtil() {
        this.discoverer = new DefaultParameterNameDiscoverer();
    }

    public void printLog(final LogLevel level, final JoinPoint joinPoint, final LogFormat logType) {
        print(level, sendLoggingMessage(logType, joinPoint, parseMessage(joinPoint)));
    }

    public void printLog(final JoinPoint joinPoint, final Exception exception, final LogFormat logType) {
        final StringBuilder sb = new StringBuilder();
        sb.append(exception.getClass().getName()).append(", ").append(exception.getMessage());
        print(LogLevel.ERROR, sendLoggingMessage(logType, joinPoint, sb.toString()));
    }

    private void print(final LogLevel level, final String logDetail) {
        switch (level) {
            case TRACE -> log.trace(logDetail);
            case DEBUG -> log.debug(logDetail);
            case WARN -> log.warn(logDetail);
            case ERROR -> log.error(logDetail);
            default -> log.info(logDetail);
        }
    }

    private String parseMessage(final JoinPoint joinPoint) {
        final String[] classPath = joinPoint.getSignature().getDeclaringType().getName().split("\\.");
        final String callerClass = classPath[classPath.length - 1];
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final String methodName = method.getName();
        final Object[] args = joinPoint.getArgs();
        final String[] parameterNames = discoverer.getParameterNames(method);

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

    private String sendLoggingMessage(final LogFormat logType, final JoinPoint joinPoint, final String responseString) {
        final String ip = ContextUtil.getRequestIp();
        final String requestUrl = ContextUtil.getRequestURL();
        final String requestMethod = ContextUtil.getRequestMethod();
        final Integer userId = ContextUtil.getUserIntraId();
        final String method = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        return LogFormat.buildLog(logType, ip, requestUrl, requestMethod, String.valueOf(userId), method, responseString);
    }
}
