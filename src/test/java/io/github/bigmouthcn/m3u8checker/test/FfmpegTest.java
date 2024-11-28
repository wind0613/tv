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
        String url = "http://[2409:8087:5e00:24::1e]:6060/000000001000/5000000006000040024/1.m3u8";
        MetaInfo metaInfo = getMetaInfo(url);
        System.out.println(metaInfo);
    }
}
