package com.example.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

/**
 * @author jinrun.xie
 * @date 2019/8/2
 **/
@Aspect
@Configuration
@Slf4j
public class ControllerAdvice {

    @Before("execution(* com.example.*.*(..))")
    public void check(JoinPoint joinPoint) {
        log.info("执行controller之前，先执行以下AOP，检查一下");
    }

}
