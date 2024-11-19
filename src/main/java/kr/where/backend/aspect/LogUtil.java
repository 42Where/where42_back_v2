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

    public void printLog(final LogLevel level, final JoinPoint joinPoint) {
        print(level, sendLoggingMessage(joinPoint, parseMessage(joinPoint)));
    }

    public void printLog(final JoinPoint joinPoint, final Exception exception) {
        print(LogLevel.ERROR, sendLoggingMessage(joinPoint, parseErrorMessage(joinPoint, exception)));
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

    private String getArgument(String[] parameterNames, Object[] args) {
        if (Objects.isNull(parameterNames) || parameterNames.length == 0) {
            return "No parameters";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(parameterNames[i])
                    .append("=")
                    .append(formatArgument(args[i]))
                    .append(", ");
        }
        return sb.toString();
    }

    private String formatArgument(Object arg) {
        if (arg == null) {
            return "null";
        }
        String argStr = arg.toString();
        return argStr.length() > 100 ? argStr.substring(0, 97) + "..." : argStr;
    }

    private String sendLoggingMessage(JoinPoint joinPoint, String responseString) {
        final String ip = ContextUtil.getRequestIp();
        final String requestUrl = ContextUtil.getRequestURL();
        final String requestMethod = ContextUtil.getRequestMethod();
        final Integer userId = ContextUtil.getUserIntraId();
        final String method = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        return LogFormat.buildLog(ip, requestUrl, requestMethod, String.valueOf(userId), method, responseString);
    }
}
