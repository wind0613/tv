package io.github.bigmouthcn.m3u8checker;

import io.github.bigmouthcn.m3u8checker.database.InfoService;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties({
        ApplicationConfig.class
})
public class Application implements WebMvcConfigurer, SchedulingConfigurer, InitializingBean {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private final InfoService infoService;

    public Application(InfoService infoService) {
        this.infoService = infoService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        infoService.createTable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        ThreadPoolTaskScheduler taskExecutor = new ThreadPoolTaskScheduler();
        taskExecutor.setPoolSize(200);
        taskExecutor.setThreadNamePrefix("async-service-");
        taskExecutor.initialize();
        configurer.setTaskExecutor(taskExecutor);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(10);
        executor.setThreadNamePrefix("async-scheduler-");
        executor.initialize();
        taskRegistrar.setTaskScheduler(executor);
    }

    @Bean
    public HttpClient httpClient() {
        return createHttpClient(2000, 200, 1000, 10000, 10000);
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return createOkHttpClient();
    }

    public static OkHttpClient createOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(Duration.ofMillis(10000))
                .readTimeout(Duration.ofMillis(10000))
                .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))
                .build();
    }

    public static HttpClient createHttpClient(int maxTotal, int defaultMaxPerRoute, int connectionRequestTimeout, int connectTimeout, int socketTimeout) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

        RequestConfig requestConfig = RequestConfig.custom()
                .setRedirectsEnabled(true)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setExpectContinueEnabled(false)
                .build();
        return HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
