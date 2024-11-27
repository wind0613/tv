package io.github.bigmouthcn.m3u8checker.checker;

import io.github.bigmouthcn.m3u8checker.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Slf4j
//@Configuration
@Deprecated
public class HttpClientM3u8Checker extends AbstractM3u8Checker {

    private final HttpClient httpClient;

    public HttpClientM3u8Checker(ApplicationConfig applicationConfig, HttpClient httpClient) {
        super(applicationConfig);
        this.httpClient = httpClient;
    }

    @Override
    protected void check0(String url) throws Exception {
        HttpGet get = null;
        try {
            get = new HttpGet(new URI(url));
            HttpResponse httpResponse = httpClient.execute(get);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new CheckFailException(FailType.NO_OK);
            }
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
    }
}
