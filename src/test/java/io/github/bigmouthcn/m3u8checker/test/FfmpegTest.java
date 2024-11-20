package io.github.bigmouthcn.m3u8checker.test;

import io.github.bigmouthcn.m3u8checker.checker.MetaInfo;
import org.junit.Test;

import static io.github.bigmouthcn.m3u8checker.utils.FfmpegUtil.getMetaInfo;

/**
 * @author Allen Hu
 * @date 2024/11/19
 */
public class FfmpegTest {

    @Test
    public void test() {
        // FFmpeg 命令
        String ffmpeg = "/Users/huxiao/Documents/program/ffmpeg-work/ffmpeg";
        String url = "http://php.jdshipin.com:8880/iptv.php?id=hnds";
        MetaInfo metaInfo = getMetaInfo(ffmpeg, url);
        System.out.println(metaInfo);
    }
}
