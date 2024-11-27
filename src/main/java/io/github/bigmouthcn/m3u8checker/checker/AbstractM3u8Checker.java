package io.github.bigmouthcn.m3u8checker.checker;

import io.github.bigmouthcn.m3u8checker.ApplicationConfig;
import io.github.bigmouthcn.m3u8checker.utils.FfmpegUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @author Allen Hu
 * @date 2024/11/27
 */
@Slf4j
public abstract class AbstractM3u8Checker implements M3u8Checker {

    private final ApplicationConfig applicationConfig;

    protected AbstractM3u8Checker(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    /**
     * @param url m3u8文件地址
     * @throws Exception 如果检查失败请抛出任意异常
     */
    protected abstract void check0(String url) throws Exception;

    @Override
    public CheckResult check(String url) throws CheckFailException {
        try {
            CheckResult result = new CheckResult().setStartTime(System.currentTimeMillis());
            check0(url);
            return result.setEndTime(System.currentTimeMillis());
        } catch (Exception e) {
            if (e instanceof ConnectTimeoutException) {
                throw new CheckFailException(FailType.CONNECT_TIMEOUT);
            } else if (e instanceof SocketTimeoutException) {
                throw new CheckFailException(FailType.SOCKET_TIMEOUT);
            } else if (e instanceof IOException) {
                throw new CheckFailException(FailType.SOCKET_ERROR);
            } else if (e instanceof CheckFailException) {
                throw (CheckFailException) e;
            }
            log.error("", e);
            throw new CheckFailException(FailType.UNKNOWN);
        }
    }

    @Override
    public MetaInfo getMetaInfo(String url) {
        String ffmpegPath = applicationConfig.getFfmpegPath();
        return FfmpegUtil.getMetaInfo(ffmpegPath, url);
    }
}
