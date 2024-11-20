package io.github.bigmouthcn.m3u8checker.checker;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Allen Hu
 * @date 2024/11/19
 */
@Data
@Accessors(chain = true)
public class MetaInfo {

    private VideoInfo videoInfo = new VideoInfo();
    private AudioInfo audioInfo = new AudioInfo();

    @Data
    @Accessors(chain = true)
    public static class VideoInfo {
        /**
         * 视频宽度
         */
        private int width = -1;
        /**
         * 视频高度
         */
        private int height = -1;
        /**
         * 视频编码器
         */
        private String codec = "UNKNOWN";
        /**
         * 帧率
         */
        private int fps = -1;

        /**
         * @param string 信息。如：（Stream #0:0: Video: h264 (High) ([27][0][0][0] / 0x001B), yuv420p, 1920x1080 [SAR 1:1 DAR 16:9], 25 fps, 25 tbr, 90k tbn）
         * @return VideoInfo
         */
        public static VideoInfo of(String string) {
            if (StringUtils.isBlank(string)) {
                return null;
            }
            string = string.trim();
            Matcher matcher = Pattern.compile("Stream.*Video:").matcher(string);
            if (matcher.find()) {
                VideoInfo e = new VideoInfo();
                // h264 (High) ([27][0][0][0] / 0x001B), yuv420p, 1920x1080 [SAR 1:1 DAR 16:9], 25 fps, 25 tbr, 90k tbn
                // h264 (High) ([27][0][0][0] / 0x001B), yuv420p(tv, bt709), 1920x1080 [SAR 1:1 DAR 16:9], 25 fps, 25 tbr, 90k tbn
                string = string.replace(matcher.group(), "");

                // 去除括号里的值
                //  h264  , yuv420p, 1920x1080 [SAR 1:1 DAR 16:9], 25 fps, 25 tbr, 90k tbn
                string = string.replaceAll("\\(.*?\\)", "");

                // [0] h264
                // [1] yuv420p
                // [2] 1920x1080 [SAR 1:1 DAR 16:9]
                // [3] 25 fps
                // [4] 25 tbr
                // [5] 90k tbn
                String[] arr = string.split(",");
                for (int i = 0; i < arr.length; i++) {
                    String s = arr[i];
                    String v = s.trim().split(" ")[0];
                    if (i == 0) {
                        e.setCodec(v);
                    } else if (i == 2) {
                        String[] wh = v.split("x");
                        e.setWidth(Integer.parseInt(wh[0]));
                        e.setHeight(Integer.parseInt(wh[1]));
                    } else if (i == 3) {
                        // maybe is float value
                        e.setFps(Math.round(Float.parseFloat(v)));
                    }
                }
                return e;
            }
            return null;
        }
    }

    @Data
    @Accessors(chain = true)
    public static class AudioInfo {

        /**
         * 编码格式
         */
        private String codec = "UNKNOWN";
        /**
         * 采样率
         */
        private int sampleRate = -1;

        /**
         * @param string 音频信息。如（Stream #0:1: Audio: aac (LC) ([15][0][0][0] / 0x000F), 48000 Hz, stereo, fltp)
         * @return AudioInfo
         */
        public static AudioInfo of(String string) {
            if (StringUtils.isBlank(string)) {
                return null;
            }
            string = string.trim();
            Matcher matcher = Pattern.compile("Stream.*Audio:").matcher(string);
            if (matcher.find()) {
                AudioInfo e = new AudioInfo();
                // aac (LC) ([15][0][0][0] / 0x000F), 48000 Hz, stereo, fltp
                string = string.replace(matcher.group(), "");
                // [0] aac (LC) ([15][0][0][0] / 0x000F)
                // [1] 48000 Hz
                // [2] stereo
                // [3] fltp
                String[] arr = string.split(",");
                for (int i = 0; i < arr.length; i++) {
                    String s = arr[i];
                    String v = s.trim().split(" ")[0];
                    if (i == 0) {
                        e.setCodec(v);
                    } else if (i == 1) {
                        e.setSampleRate(Integer.parseInt(v.replace("Hz", "")));
                    }
                }
                return e;
            }
            return null;
        }
    }
}
