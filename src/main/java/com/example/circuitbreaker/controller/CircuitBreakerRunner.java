package com.example.circuitbreaker.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class CircuitBreakerRunner {

    public static String run(CircuitBreaker circuitBreaker, Callable callable) throws Exception {
        log.info("参数：circuitBreaker = {}", circuitBreaker.toString());
        final Object call = callable.call();
        log.info("回调执行结果：{}", call);
        return "网络断线中，请稍后重试。。。";
    }

}
