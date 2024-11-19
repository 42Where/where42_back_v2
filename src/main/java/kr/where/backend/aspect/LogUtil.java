package kr.where.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.net.URI;
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

    public void printLog(final LogLevel level, final JoinPoint joinPoint,
                         final Exception exception, final LogFormat logType) {
        print(level, sendLoggingMessage(logType, joinPoint, parseErrorMessage(joinPoint, exception)));
    }

    public void printLog(final LogLevel level, final JoinPoint joinPoint, final String executeTime, final LogFormat logType) {
        print(level, sendLoggingMessage(logType, joinPoint, executeTime));
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
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final Object[] args = joinPoint.getArgs();
        final String[] parameterNames = discoverer.getParameterNames(method);

        StringBuilder sb = new StringBuilder();
        sb.append(getArgument(parameterNames, args));
        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

    private String parseErrorMessage(final JoinPoint joinPoint, final Exception exception) {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final Object[] args = joinPoint.getArgs();
        final String[] parameterNames = discoverer.getParameterNames(method);

        StringBuilder sb = new StringBuilder();
        sb.append(exception.getClass().getName()).append(", ").append(exception.getMessage());
        sb.append(getArgument(parameterNames, args));
        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

    private String getArgument(final String[] parameterNames, final Object[] args) {
        if (Objects.isNull(parameterNames) || parameterNames.length == 0) {
            return "No parameters";
        }
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(formatArgument(arg))
                    .append(", ");
        }
        return sb.toString();
    }

    private String formatArgument(final Object arg) {
        if (arg == null) {
            return "null";
        }
        final String argStr = arg.toString();
        return argStr.length() > 100 ? argStr.substring(0, 97) + "..." : argStr;
    }

    private String sendLoggingMessage(final LogFormat logType, final JoinPoint joinPoint, final String responseString) {
        final String ip = ContextUtil.getRequestIp();
        final String requestUrl = ContextUtil.getRequestURL();
        final String path = URI.create(requestUrl).getPath();
        final String requestMethod = ContextUtil.getRequestMethod();
        final Integer userId = ContextUtil.getUserIntraId();
        final String method = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        return LogFormat.buildLog(logType, ip, path, requestMethod, String.valueOf(userId), method, responseString);
    }
}
