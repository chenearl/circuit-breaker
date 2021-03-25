package com.example.circuitbreaker.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Aspect
@Slf4j
@Component
public class CircuitBreakerAspect {

    @Autowired
    private CircuitBreaker circuitBreaker;

    /**
     * 上次断开的时间
     */
    private long lastBreakerTime = 0;

    @Around("execution(public * com.example.circuitbreaker.controller.CircuitController.execute(..))")
    public Object around(ProceedingJoinPoint pj) throws Throwable {
        try {
            long offset = System.currentTimeMillis() - lastBreakerTime;
            if ((offset < circuitBreaker.getDelay())) {
                return CircuitBreakerRunner.run(circuitBreaker, () -> {
                    System.out.println("执行回调！");
                    return "hello";
                });
            } else if (circuitBreaker.getRequestCount().get() >= circuitBreaker.getTotal()
                    || circuitBreaker.checkBreaker()) {
                circuitBreaker.reset();
            }
            circuitBreaker.getRequestCount().incrementAndGet();
            return pj.proceed();
        } catch (Exception e) {
            log.error("业务异常，捕获成功！");
            circuitBreaker.getErrorCount().incrementAndGet();
            // 检查是否断开
            if (circuitBreaker.checkBreaker()) {
                log.info("开启延时");
                lastBreakerTime = System.currentTimeMillis();
            }
            return "发生异常";
        }
    }
}
