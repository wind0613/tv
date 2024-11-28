package io.github.bigmouthcn.m3u8checker.schedule;

import io.github.bigmouthcn.m3u8checker.checker.M3u8;
import io.github.bigmouthcn.m3u8checker.checker.M3u8Checker;
import io.github.bigmouthcn.m3u8checker.checker.MetaInfo;
import io.github.bigmouthcn.m3u8checker.database.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Objects;

/**
 * @author Allen Hu
 * @date 2024-11-19
 */
@Slf4j
@Configuration
public class MetaInfoResolveScheduler implements Scheduler {

    private final M3u8Checker m3u8Checker;
    private final InfoService infoService;

    public MetaInfoResolveScheduler(M3u8Checker m3u8Checker, InfoService infoService) {
        this.m3u8Checker = m3u8Checker;
        this.infoService = infoService;
    }

    @Override
    @Scheduled(initialDelay = 0, fixedDelay = 10 * 60 * 1000)
    public void schedule() {
        List<M3u8> all = infoService.findAll();
        for (M3u8 m3u8 : all) {
            try {
                if (!m3u8.isOk()) {
                    continue;
                }
                MetaInfo metaInfo = m3u8Checker.getMetaInfo(m3u8.getUrl());
                if (Objects.nonNull(metaInfo) && metaInfo.isValid()) {
                    infoService.updateMetaInfo(m3u8, metaInfo);
                }
            } catch (Exception e) {
                log.error("resolve meta info error", e);
            }
        }
    }
}
