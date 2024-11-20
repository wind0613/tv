package io.github.bigmouthcn.m3u8checker.controller;

import io.github.bigmouthcn.m3u8checker.schedule.RemoteFileCheckScheduler;
import io.github.bigmouthcn.m3u8checker.schedule.Scheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Allen Hu
 * @date 2024/11/18
 */
@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    private final ApplicationContext applicationContext;
    private final RemoteFileCheckScheduler remoteFileCheckScheduler;

    public SchedulerController(ApplicationContext applicationContext, RemoteFileCheckScheduler remoteFileCheckScheduler) {
        this.applicationContext = applicationContext;
        this.remoteFileCheckScheduler = remoteFileCheckScheduler;
    }

    @GetMapping("/execute")
    public ResponseEntity<Object> execute(String beanName) {
        Scheduler scheduler = applicationContext.getBean(beanName, Scheduler.class);
        scheduler.asyncSchedule();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkList")
    public ResponseEntity<Object> checkList(String source) {
        remoteFileCheckScheduler.execute(source);
        return ResponseEntity.ok().build();
    }
}
