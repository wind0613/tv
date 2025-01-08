package io.github.bigmouthcn.m3u8checker.schedule;

import cn.hutool.core.io.IoUtil;
import io.github.bigmouthcn.m3u8checker.checker.*;
import io.github.bigmouthcn.m3u8checker.database.InfoService;
import io.github.bigmouthcn.m3u8checker.parser.M3uParserFactory;
import io.github.bigmouthcn.m3u8checker.parser.M3uSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Slf4j
@Configuration
public class RemoteFileCheckScheduler implements Scheduler {

    private static final String listSource = "https://gh-proxy.com/https://raw.githubusercontent.com/big-mouth-cn/tv/main/my-m3u-list.txt";

    private final M3uParserFactory m3uParserFactory;
    private final M3u8Checker m3u8Checker;
    private final InfoService infoService;
    private final HttpClient httpClient;

    public RemoteFileCheckScheduler(M3uParserFactory m3uParserFactory, M3u8Checker m3u8Checker, InfoService infoService, HttpClient httpClient) {
        this.m3uParserFactory = m3uParserFactory;
        this.m3u8Checker = m3u8Checker;
        this.infoService = infoService;
        this.httpClient = httpClient;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void schedule() {
        log.info("start remote file check schedule.");
        List<String> sources = getSources();
        for (String source : sources) {
            checkM3uList(source);
        }
    }

    @Async
    public void execute(String source) {
        this.checkM3uList(source);
    }

    private void checkM3uList(String source) {
        try {
            log.info("checking source: {}", source);
            List<M3u8> m3u8List = m3uParserFactory.getParser(M3uSource.URL).parse(source);
            if (CollectionUtils.isEmpty(m3u8List)) {
                log.info("no m3u8 found for {}", source);
                return;
            }
            for (M3u8 m3u8 : m3u8List) {
                CheckResult checked = null;
                FailType failType = null;

                try {
                    checked = m3u8Checker.check(m3u8.getUrl());
                } catch (CheckFailException e) {
                    failType = e.getFailType();
                }
                infoService.updateCheckResult(m3u8, checked, failType);

                String msg = Objects.nonNull(failType) ? failType.name() : "OK";
                log.info("checked {} for {} - {}", msg, m3u8.getTitle(), m3u8.getUrl());
            }
            log.info("finished: {}", source);
        } catch (Exception e) {
            log.error("check source {} error", source, e);
        }
    }

    private List<String> getSources() {
        HttpGet get = null;
        List<String> rs = new ArrayList<>();
        try {
            get = new HttpGet(listSource);
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                 IoUtil.readUtf8Lines(response.getEntity().getContent(), rs);
            }
        } catch (Exception e) {
            log.error("get list source error", e);
        } finally {
            if (Objects.nonNull(get)) {
                get.releaseConnection();
            }
        }
        return rs;
    }
}
