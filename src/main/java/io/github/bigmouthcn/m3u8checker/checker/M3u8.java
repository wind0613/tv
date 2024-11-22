package io.github.bigmouthcn.m3u8checker.checker;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Data
@Accessors(chain = true)
public class M3u8 {

    public static final String STATUS_DELETED = "DELETED";

    /**
     * database id
     */
    private Integer id;

    private String tvgId;
    private String tvgName;
    private String tvgLogo;
    private String groupTitle;
    private String title;
    private String url;
    private String status;
    private String costTime;
    private String lastCheckTime;

    private Integer videoWidth;
    private Integer videoHeight;
    private String videoCodec;
    private Integer videoFps;
    private String audioCodec;
    private Integer audioSampleRate;

    public boolean isOk() {
        return StringUtils.isBlank(status);
    }

    public String convert2M3u8Line() {
        // #EXTINF:-1 group-title="湖南台" tvg-id="湖南爱晚" tvg-logo="https://live.fanmingming.com/tv/%E6%B9%96%E5%8D%97%E7%88%B1%E6%99%9A.png",湖南爱晚
        // http://phonetx.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwMy4zOS4yMjYuMTAwJnFpZD0mY2RuZXhfaWQ9dHhfcGhvbmVfbGl2ZSZzPWUyNTY0MGRmY2M4MjAyNzY2YmI4NDNjNDIxNWUyZGJmJnVpZD0mdXVpZD04ODNkOTQ4NTljMzE2MWEyZTIyYjc5ODBlMjFiYjE2Ny02NzI3ZTI2NCZ2PTImYXM9MCZlcz0xNzMxOTE1Mzkz/HNGGMPP360.m3u8
        return "#EXTINF:-1 group-title=\"" + groupTitle
                + "\" tvg-id=\"" + tvgId
                + "\" tvg-name=\"" + tvgName
                + "\" tvg-logo=\"" + tvgLogo
                + "\" t-time=\"" + costTime
                + "\" v-w=\"" + videoWidth
                + "\" v-h=\"" + videoHeight
                + "\", "
                + title + "\n" + url;
    }
}
