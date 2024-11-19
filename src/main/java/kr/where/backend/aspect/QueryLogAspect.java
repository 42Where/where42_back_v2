package kr.where.backend.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class QueryLogAspect {
    private final LogUtil logUtil;

    @Around("execution(* kr.where.backend..*Repository.*(..))")
    public Object measureJpaExecutionTime(final ProceedingJoinPoint point) throws Throwable {

        try {
            final long startTime = System.currentTimeMillis();
            final Object result = point.proceed();
            final long executionTime = System.currentTimeMillis() - startTime;

            logUtil.printLog(LogLevel.INFO,
                    point,
                    String.format(
                            LogFormat.MS.getFormat(),
                            executionTime
                    ),
                    LogFormat.QUERY
            );
            return result;
        } catch (final SQLException e) {
            logUtil.printLog(point, e, LogFormat.QUERY);
            throw e;
        }
    }
}