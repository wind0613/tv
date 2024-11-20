package io.github.bigmouthcn.m3u8checker.parser;

import cn.hutool.core.io.IoUtil;
import io.github.bigmouthcn.m3u8checker.checker.M3u8;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Slf4j
@Configuration
public class UrlM3uParser implements M3uParser {

    private final HttpClient httpClient;

    public UrlM3uParser(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public M3uSource getSource() {
        return M3uSource.URL;
    }

    @Override
    public List<M3u8> parse(String m3uUrl) {
        HttpGet get = null;
        try {
            get = new HttpGet(m3uUrl);
            HttpResponse httpResponse = httpClient.execute(get);
            InputStream inputStream = httpResponse.getEntity().getContent();
            List<String> lines = new ArrayList<>();
            IoUtil.readLines(inputStream, Charset.defaultCharset(), lines);
            return parse(lines);
        } catch (Exception e) {
            log.error("parse m3u8 failed", e);
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        return null;
    }
}
