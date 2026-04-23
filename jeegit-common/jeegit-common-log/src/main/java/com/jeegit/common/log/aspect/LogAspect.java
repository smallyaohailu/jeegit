package com.jeegit.common.log.aspect;

import com.jeegit.common.core.utils.JsonUtils;
import com.jeegit.common.core.utils.ServletUtils;
import com.jeegit.common.log.annotation.Log;
import com.jeegit.common.log.event.OperLogEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final ApplicationEventPublisher eventPublisher;

    @Around("@annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        OperLogEvent event = new OperLogEvent();
        event.setOperTime(LocalDateTime.now());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        event.setTitle(logAnnotation.title());
        event.setBusinessType(logAnnotation.businessType().getDescription());
        event.setMethod(signature.getDeclaringTypeName() + "." + signature.getName() + "()");

        try {
            HttpServletRequest request = ServletUtils.getRequest();
            event.setRequestMethod(request.getMethod());
            event.setRequestUrl(request.getRequestURI());
            event.setClientIp(ServletUtils.getClientIp());
            event.setOperName(request.getHeader("X-User-Name"));
        } catch (Exception e) {
            log.warn("Failed to obtain servlet request context", e);
        }

        if (logAnnotation.isSaveRequestData()) {
            try {
                event.setRequestParam(JsonUtils.toJson(joinPoint.getArgs()));
            } catch (Exception e) {
                log.warn("Failed to serialize request parameters", e);
            }
        }

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            event.setStatus(0);
            if (logAnnotation.isSaveResponseData()) {
                try {
                    event.setResponseResult(JsonUtils.toJson(result));
                } catch (Exception e) {
                    log.warn("Failed to serialize response result", e);
                }
            }
        } catch (Throwable ex) {
            event.setStatus(1);
            event.setErrorMsg(ex.getMessage());
            throw ex;
        } finally {
            event.setCostTime(System.currentTimeMillis() - startTime);
            eventPublisher.publishEvent(event);
        }
        return result;
    }
}
