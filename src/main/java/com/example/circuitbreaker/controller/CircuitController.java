package com.example.circuitbreaker.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
@Slf4j
public class CircuitController {

    AtomicInteger atomicInteger = new AtomicInteger(0);

    @GetMapping("/circuit")
    public String execute() throws Exception {
        atomicInteger.incrementAndGet();
        if (atomicInteger.get() % 2 == 0) {
            throw new Exception("模拟异常");
        }
        log.info("执行成功！");
        return "hello world";
    }

}
