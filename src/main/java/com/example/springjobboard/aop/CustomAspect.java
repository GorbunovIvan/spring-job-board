package com.example.springjobboard.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class CustomAspect {

//    @Around("Pointcuts.allEndpoints()") - it breaks dependency injection.
//    This pointcut and @ModelAttribute methods of controller cannot work together
    public Object aroundEndpointAdvice(ProceedingJoinPoint joinPoint) {
        return defaultHandlingJoinPoint(joinPoint, "controller", "endpoint-method");
    }

    @Around("Pointcuts.allRepositoriesMethods()")
    public Object aroundRepositoryMethodAdvice(ProceedingJoinPoint joinPoint) {
        return defaultHandlingJoinPoint(joinPoint, "repository");
    }

    @Around("Pointcuts.allModelsMethods()")
    public Object aroundModelMethodAdvice(ProceedingJoinPoint joinPoint) {
        return defaultHandlingJoinPoint(joinPoint, "model");
    }

    @Around("Pointcuts.allUtilsMethods()")
    public Object aroundUtilMethodAdvice(ProceedingJoinPoint joinPoint) {
        return defaultHandlingJoinPoint(joinPoint, "class");
    }

    @Around("Pointcuts.allSecurityMethods()")
    public Object aroundSecurityMethodAdvice(ProceedingJoinPoint joinPoint) {
        return defaultHandlingJoinPoint(joinPoint, "security-class");
    }

    private Object defaultHandlingJoinPoint(ProceedingJoinPoint joinPoint, String layer) {
        return defaultHandlingJoinPoint(joinPoint, layer, "method");
    }

    private Object defaultHandlingJoinPoint(ProceedingJoinPoint joinPoint,
                                                         String layer, String methodDesignation) {

        var signature = (MethodSignature) joinPoint.getSignature();

        String message = String.format("%s '%s', %s '%s'",
                layer,
                signature.getMethod().getDeclaringClass().getName(),
                methodDesignation,
                signature.getName());

        Object result;

        try {
            log.info(message + " is invoked");
            result = joinPoint.proceed();
            log.info(message + " is completed");
        } catch (RuntimeException e) {
            log.error(message + " has thrown an error");
            throw e;
        } catch (Throwable e) {
            log.error(message + " has thrown an error");
            throw new RuntimeException(e);
        }

        return result;
    }
}
