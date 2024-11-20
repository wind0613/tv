package io.github.bigmouthcn.m3u8checker.schedule;

import org.springframework.scheduling.annotation.Async;

public interface Scheduler {

    @Async
    default void asyncSchedule() {
        this.schedule();
    }

    void schedule();
}
