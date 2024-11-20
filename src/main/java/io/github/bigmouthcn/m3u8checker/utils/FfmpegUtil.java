package io.github.bigmouthcn.m3u8checker.utils;

import cn.hutool.core.io.IoUtil;
import io.github.bigmouthcn.m3u8checker.checker.MetaInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Allen Hu
 * @date 2024/11/19
 */
@Slf4j
public class FfmpegUtil {

    public static MetaInfo getMetaInfo(String url) {
        return getMetaInfo("ffmpeg", url);
    }

    public static MetaInfo getMetaInfo(String ffmpeg, String url) {
        InputStreamReader in = null;
        BufferedReader reader = null;
        String line = null;
        try {
            MetaInfo metaInfo = new MetaInfo();
            String command = String.format("%s -i %s", ffmpeg, url);
            Process process = Runtime.getRuntime().exec(command);
            in = new InputStreamReader(process.getErrorStream());
            reader = new BufferedReader(in);

            while ((line = reader.readLine()) != null) {
                log.debug(line);

                MetaInfo.VideoInfo videoInfo = MetaInfo.VideoInfo.of(line);
                if (videoInfo != null) {
                    metaInfo.setVideoInfo(videoInfo);
                }
                MetaInfo.AudioInfo audioInfo = MetaInfo.AudioInfo.of(line);
                if (audioInfo != null) {
                    metaInfo.setAudioInfo(audioInfo);
                }
            }
            return metaInfo;
        } catch (Exception e) {
            log.error("getMetaInfo occur exception:\n" +
                    "URL:{}\n" +
                    "Line String:{}"
                    , url, line, e);
            return null;
        } finally {
            IoUtil.close(reader);
            IoUtil.close(in);
        }
    }
}
