package com.dimi.server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@CrossOrigin("*")
@RequestMapping("/sse")
public class SseController {
    @GetMapping
    public SseEmitter sse() {
        SseEmitter sseEmitter = new SseEmitter();
        ExecutorService sseExecutor = Executors.newSingleThreadExecutor();
        sseExecutor.execute(() -> sseProcess(sseEmitter));
        return sseEmitter;
    }

    /**
     * Simulates a process being executed. Sends a JSON with the structure {"value": 'percentage'}, where 'percentage'
     * is the current progress percentage of the process
     * @param sseEmitter server send emitter for sending the progress value to the client
     */
    private void sseProcess(SseEmitter sseEmitter) {
        try {
            for (int i = 0; i <= 100; i = progressValue(i)) {
                SseEventBuilder sseEventBuilder = SseEmitter.event().data(String.format("{\"value\": %s}", i));
                sseEmitter.send(sseEventBuilder);
                if (i == 100) break;
                Thread.sleep(randomInt(250, 1000));
            }
            sseEmitter.complete();
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
        }
    }

    /**
     *
     * @param currentValue current progress value
     * @return new progress value, never exceeding 100
     */
    private int progressValue(int currentValue) {
        int newValue = currentValue + randomInt(5, 20);
        return Math.min(newValue, 100);
    }

    /**
     *
     * @param minValue minimum value that the int can have (inclusive)
     * @param maxValue maximum value that the int can have (inclusive)
     * @return int value between minValue and maxValue
     */
    private int randomInt(int minValue, int maxValue) {
        return new Random().nextInt(maxValue - minValue + 1) + minValue;
    }

}
