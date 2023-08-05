package com.example.springjobboard.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(public String com.example.springjobboard.controller.*Controller.*(..))")
    public void allEndpoints() {}

    @Pointcut("execution(* com.example.springjobboard.repository.*.*(..))")
    public void allRepositoriesMethods() {}

    @Pointcut("execution(* com.example.springjobboard.model.*.*(..))")
    public void allModelsMethods() {}

    @Pointcut("execution(* com.example.springjobboard.util.*.*(..))")
    public void allUtilsMethods() {}

    @Pointcut("execution(* com.example.springjobboard.security.*.*(..))")
    public void allSecurityMethods() {}
}
