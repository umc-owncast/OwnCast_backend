package com.umc.owncast.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(com.umc.owncast.common.annotation.TrackExecutionTime)" +
            "|| @within(com.umc.owncast.common.annotation.TrackExecutionTime)")
    public Object trackExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드 실행 전후로 실행시간 측정
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(); // 기존 메서드 실행
        long end = System.currentTimeMillis();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info("{}: {}() executed in {}ms", className, methodName, end - start);
        return proceed;
    }
}
