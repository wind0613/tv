package io.github.bigmouthcn.m3u8checker;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Allen Hu
 * @date 2024/11/19
 */
@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {

    private String ffmpegPath = "ffmpeg";
}
