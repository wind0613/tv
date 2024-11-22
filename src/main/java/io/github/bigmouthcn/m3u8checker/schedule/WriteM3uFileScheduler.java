package io.github.bigmouthcn.m3u8checker.schedule;

import cn.hutool.core.io.FileUtil;
import io.github.bigmouthcn.m3u8checker.checker.M3u8;
import io.github.bigmouthcn.m3u8checker.database.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author Allen Hu
 * @date 2024-11-22
 */
@Slf4j
@Configuration
public class WriteM3uFileScheduler implements Scheduler {

    private final InfoService infoService;

    public WriteM3uFileScheduler(InfoService infoService) {
        this.infoService = infoService;
    }

    @Override
    @Scheduled(cron = "0 */10 * * * ?")
    public void schedule() {
        try {
            StringBuilder allChannel = new StringBuilder();
            allChannel.append("#EXTM3U").append("\n");

            StringBuilder justOk = new StringBuilder();
            justOk.append("#EXTM3U").append("\n");

            List<M3u8> all = infoService.findAll();
            for (M3u8 m3u8 : all) {
                String line = m3u8.convert2M3u8Line();
                if (m3u8.isOk()) {
                    justOk.append(line).append("\n");
                }
                allChannel.append(line).append("\n");
            }
            FileUtil.writeString(allChannel.toString(), "iptv.m3u", "UTF-8");
            FileUtil.writeString(justOk.toString(), "iptv-ok.m3u", "UTF-8");
            log.info("WriteU38FileScheduler success");
        } catch (Exception e) {
            log.error("WriteU38FileScheduler error", e);
        }
    }
}
