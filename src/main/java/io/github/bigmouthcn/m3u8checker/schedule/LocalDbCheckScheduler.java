package io.github.bigmouthcn.m3u8checker.schedule;

import io.github.bigmouthcn.m3u8checker.checker.*;
import io.github.bigmouthcn.m3u8checker.database.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Objects;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Slf4j
@Configuration
public class LocalDbCheckScheduler implements Scheduler {

    private final M3u8Checker m3u8Checker;
    private final InfoService infoService;

    public LocalDbCheckScheduler(M3u8Checker m3u8Checker, InfoService infoService) {
        this.m3u8Checker = m3u8Checker;
        this.infoService = infoService;
    }

    @Override
    @Scheduled(initialDelay = 0, fixedDelay = 5 * 60 * 1000)
    public void schedule() {
        log.info("start local database check schedule.");

        List<M3u8> all = infoService.findAll();
        for (M3u8 m3u8 : all) {
            try {
                CheckResult checked = null;
                FailType failType = null;

                try {
                    checked = m3u8Checker.check(m3u8.getUrl());
                } catch (CheckFailException e) {
                    failType = e.getFailType();
                }
                infoService.updateCheckResult(m3u8, checked, failType);

                String msg = Objects.nonNull(failType) ? failType.name() : "OK";
                log.info("checked {} for {} - {}", msg, m3u8.getTvgName(), m3u8.getUrl());
            } catch (Exception e) {
                log.error("CheckScheduler schedule error", e);
            }
        }
        log.info("finished!");
    }
}
