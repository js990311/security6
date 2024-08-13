package com.study.security6.security.authorization.method.aspect;

import com.study.security6.security.authorization.method.annotation.BoardAuthorization;
import com.study.security6.security.authorization.method.annotation.CrudMethod;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class TestMethodAspect {
    @Around("@annotation(com.study.security6.security.authorization.method.annotation.BoardAuthorization)")
    public Object parseTest(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BoardAuthorization annotation = method.getAnnotation(BoardAuthorization.class);
        String boardIdparamName = annotation.boardId();
        CrudMethod crud = annotation.method();
        Long boardId = -1l;

        String[] params = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for(int i=0;i<params.length;i++){
            if(boardIdparamName.equals(params[i])){
                boardId = (Long) args[i];
            }
        }
        if(boardId == -1){
            return null;
        }


        return joinPoint.proceed();
    }
}
