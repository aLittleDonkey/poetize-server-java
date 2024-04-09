package com.ld.poetry.aop;

import com.ld.poetry.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;


@Aspect
@Component
@Order(2)
@Slf4j
public class ResourceCheckAspect {

    @Autowired
    private CommonQuery commonQuery;

    @Value("${resource.article.doc:}")
    private List<String> articleDoc;

    @Around("@annotation(resourceCheck)")
    public Object around(ProceedingJoinPoint joinPoint, ResourceCheck resourceCheck) throws Throwable {
        return joinPoint.proceed();
    }
}
