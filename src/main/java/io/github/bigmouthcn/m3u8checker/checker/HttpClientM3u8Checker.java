package io.github.bigmouthcn.m3u8checker.checker;

import io.github.bigmouthcn.m3u8checker.ApplicationConfig;
import io.github.bigmouthcn.m3u8checker.utils.FfmpegUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.context.annotation.Configuration;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Slf4j
//@Configuration
@Deprecated
public class HttpClientM3u8Checker implements M3u8Checker {

    private final HttpClient httpClient;
    private final ApplicationConfig applicationConfig;

    public HttpClientM3u8Checker(HttpClient httpClient, ApplicationConfig applicationConfig) {
        this.httpClient = httpClient;
        this.applicationConfig = applicationConfig;
    }

    @Override
    public CheckResult check(String url) throws CheckFailException {
        CheckResult result = new CheckResult().setStartTime(System.currentTimeMillis());
        HttpGet get = null;
        try {
            get = new HttpGet(new URI(url));
            HttpResponse httpResponse = httpClient.execute(get);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
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
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
    }

    @Override
    public MetaInfo getMetaInfo(String url) {
        String ffmpegPath = applicationConfig.getFfmpegPath();
        return FfmpegUtil.getMetaInfo(ffmpegPath, url);
    }
}
