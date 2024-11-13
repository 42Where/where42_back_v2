package kr.where.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class JpaPerformanceAspect {

    @Around("execution(* kr.where.backend..*Repository.*(..))")
    public Object measureJpaExecutionTime(final ProceedingJoinPoint point) throws Throwable{
        final long startTime = System.currentTimeMillis();

        Object result = point.proceed();
        final long executionTime = System.currentTimeMillis() - startTime;

        log.info("Jpa query execution time : {}ms, doing method : {}", executionTime, point.getSignature());

        return result;
    }
}
