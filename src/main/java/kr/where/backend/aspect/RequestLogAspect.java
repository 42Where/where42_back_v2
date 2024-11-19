package kr.where.backend.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestLogAspect {
    private static final String POINTCUT = "@annotation(kr.where.backend.aspect.RequestLogging)";
    private final LogUtil logUtil;

    @AfterReturning(value = "@annotation(requestLogging)", argNames = "joinPoint, requestLogging")
    public void requestSuccessLog(final JoinPoint joinPoint, final RequestLogging requestLogging) {
        logUtil.printLog(requestLogging.level(), joinPoint);
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "exception", argNames = "joinPoint, exception")
    public void requestFailureLog(final JoinPoint joinPoint, final Exception exception) {
        logUtil.printLog(joinPoint, exception);
    }
}
