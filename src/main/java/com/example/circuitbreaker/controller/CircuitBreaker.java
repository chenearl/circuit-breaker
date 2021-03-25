package com.example.circuitbreaker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class CircuitBreaker {

    @Value("${circuit.total}")
    private int total;

    @Value("${circuit.threshold}")
    private int threshold;

    @Value("${circuit.delay}")
    private long delay;

    private AtomicInteger requestCount = new AtomicInteger(0);

    private AtomicInteger errorCount = new AtomicInteger(0);

    public boolean checkBreaker() {
        if (errorCount.get() >= threshold && requestCount.get() <= total ) {
            return true;
        }
        return false;
    }

    public void reset() {
        log.info("重置了参数");
        requestCount.set(0);
        errorCount.set(0);
    }

    public long getDelay() {
        return delay;
    }

    public AtomicInteger getRequestCount() {
        return requestCount;
    }

    public AtomicInteger getErrorCount() {
        return errorCount;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "CircuitBreaker{" +
                "total=" + total +
                ", threshold=" + threshold +
                ", delay=" + delay +
                ", requestCount=" + requestCount +
                ", errorCount=" + errorCount +
                '}';
    }
}
