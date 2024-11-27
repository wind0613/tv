package io.github.bigmouthcn.m3u8checker.checker;

import io.github.bigmouthcn.m3u8checker.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2024/11/20
 */
@Slf4j
@Configuration
public class OkHttpM3u8Checker extends AbstractM3u8Checker {

    private final OkHttpClient okHttpClient;

    public OkHttpM3u8Checker(ApplicationConfig applicationConfig, OkHttpClient okHttpClient) {
        super(applicationConfig);
        this.okHttpClient = okHttpClient;
    }

    @Override
    protected void check0(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new CheckFailException(FailType.NO_OK);
            }
        }
    }
}
