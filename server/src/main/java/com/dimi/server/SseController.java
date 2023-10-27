package com.dimi.serversentevents;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class SseController {
    @GetMapping("/sse")
    @CrossOrigin("*")
    public SseEmitter sse() {
        SseEmitter sseEmitter = new SseEmitter();
        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        sseExecutor.execute(() -> {
            try {
                for (int i = 0; i < 100; i *= 10) {
                    SseEventBuilder sseEventBuilder = SseEmitter.event().data(String.format("{\"value\": %s}", i));
                    sseEmitter.send(sseEventBuilder);
                    Thread.sleep(1000);
                }
                sseEmitter.complete();
            } catch (Exception e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
    }

}
