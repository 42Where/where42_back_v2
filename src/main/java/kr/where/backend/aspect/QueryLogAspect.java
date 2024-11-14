package kr.where.backend.aspect;

import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.auth.authUser.AuthUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class QueryLogAspect {

//    @Around("execution(* kr.where.backend..*Repository.*(..))")
//    public Object measureJpaExecutionTime(final ProceedingJoinPoint point) throws Throwable {
//        final ServletRequestAttributes attributes
//                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        final long startTime = System.currentTimeMillis();
//
//        final Object result = point.proceed();
//        final long executionTime = System.currentTimeMillis() - startTime;
//        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication();
//
//        if (attributes != null) {
//            log.info("Ip : {}, Request URL : {}, Request Method : {}, UserId : {}, Msg : {}, Execute Method : {}",
//                    attributes.getRequest().getRemoteAddr(),
//                    attributes.getRequest().getRequestURL().toString(),
//                    attributes.getRequest().getMethod(),
//                    authUser.getIntraId(),
//                    "Query Execute time : " + executionTime + "ms",
//                    point.getSignature()
//            );
//        }
//        return result;
//    }
}
