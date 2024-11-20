package io.github.bigmouthcn.m3u8checker.checker;

import io.github.bigmouthcn.m3u8checker.ApplicationConfig;
import io.github.bigmouthcn.m3u8checker.utils.FfmpegUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.context.annotation.Configuration;

import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @author Allen Hu
 * @date 2024/11/20
 */
@Slf4j
@Configuration
public class OkHttpM3u8Checker implements M3u8Checker {

    private final OkHttpClient okHttpClient;
    private final ApplicationConfig applicationConfig;

    public OkHttpM3u8Checker(OkHttpClient okHttpClient, ApplicationConfig applicationConfig) {
        this.okHttpClient = okHttpClient;
        this.applicationConfig = applicationConfig;
    }

    @Override
    public CheckResult check(String url) throws CheckFailException {
        CheckResult result = new CheckResult().setStartTime(System.currentTimeMillis());
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new CheckFailException(FailType.NO_OK);
            }
            return result.setEndTime(System.currentTimeMillis());
        } catch (Exception e) {
            if (e instanceof ConnectTimeoutException) {
                throw new CheckFailException(FailType.CONNECT_TIMEOUT);
            } else if (e instanceof SocketTimeoutException) {
                throw new CheckFailException(FailType.SOCKET_TIMEOUT);
            } else if (e instanceof SocketException) {
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
