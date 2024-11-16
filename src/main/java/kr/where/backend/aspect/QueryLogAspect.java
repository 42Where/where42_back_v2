package kr.where.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Aspect
@Component
@Slf4j
public class QueryLogAspect {

    @Around("@annotation(kr.where.backend.aspect.QueryLog)")
    public Object measureJpaExecutionTime(final ProceedingJoinPoint point) throws Throwable {
        final ContextUtil contextUtil = new ContextUtil();

        try {
            final long startTime = System.currentTimeMillis();
            final Object result = point.proceed();
            final long executionTime = System.currentTimeMillis() - startTime;

            log.info("Ip : {}, Request URL : {}, Request Method : {}, UserId : {}, Msg : {}, Execute Method : {}",
                    contextUtil.getRequestIp(),
                    contextUtil.getRequestURL(),
                    contextUtil.getRequestMethod(),
                    contextUtil.getUserIntraId(),
                    "Query Execute time : " + executionTime + "ms",
                    point.getSignature()
            );
            return result;
        } catch (SQLException e) {
            log.error("Ip : {}, Request URL : {}, Request Method : {}, UserId : {}, Error Msg : {}",
                    contextUtil.getRequestIp(),
                    contextUtil.getRequestURL(),
                    contextUtil.getRequestMethod(),
                    contextUtil.getUserIntraId(),
                    e.getMessage()
            );
            throw e;
        }
    }


}
