package kr.where.backend.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestLogAspect {
    private static final String POINTCUT = "@annotation(kr.where.backend.aspect.RequestLogging) || @within(kr.where.backend.aspect.RequestLogging)";
    private final LogUtil logUtil;

    @AfterReturning(value = POINTCUT, argNames = "joinPoint")
    public void requestSuccessLog(final JoinPoint joinPoint) {
        RequestLogging requestLogging = getRequestLoggingAnnotation(joinPoint);
        logUtil.printLog(requestLogging.level(), joinPoint, LogFormat.SERVICE);
    }

    @AfterThrowing(value = POINTCUT, throwing = "exception", argNames = "joinPoint, exception")
    public void requestFailureLog(final JoinPoint joinPoint, final Exception exception) {
        logUtil.printLog(joinPoint, exception, LogFormat.SERVICE);
    }


    // JoinPoint에서 RequestLogging 애노테이션을 추출하는 메서드
    private RequestLogging getRequestLoggingAnnotation(JoinPoint joinPoint) {
        // 메서드의 애노테이션을 가져옵니다.
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 메서드에 @RequestLogging 애노테이션이 붙어 있으면 그것을 반환
        if (method.isAnnotationPresent(RequestLogging.class)) {
            return method.getAnnotation(RequestLogging.class);
        }

        // 클래스에 애노테이션이 있을 경우 반환
        Class<?> targetClass = joinPoint.getTarget().getClass();
        if (targetClass.isAnnotationPresent(RequestLogging.class)) {
            return targetClass.getAnnotation(RequestLogging.class);
        }

        return null;
    }
}
