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

    /**
     * get tvgName
     * @return 返回频道名称，从 tvgName > title > tvgId 中获取。
     */
    public String getTvgName() {
        if (StringUtils.isBlank(tvgName)) {
            tvgName = title;
        }
        if (StringUtils.isBlank(tvgName)) {
            tvgName = tvgId;
        }
        return tvgName;
    }

    public String convert2M3u8Line() {
        // #EXTINF:-1 group-title="湖南台" tvg-id="湖南爱晚" tvg-logo="https://live.fanmingming.com/tv/%E6%B9%96%E5%8D%97%E7%88%B1%E6%99%9A.png",湖南爱晚
        // http://phonetx.qing.mgtv.com/nn_live/nn_x64/dWlwPTEwMy4zOS4yMjYuMTAwJnFpZD0mY2RuZXhfaWQ9dHhfcGhvbmVfbGl2ZSZzPWUyNTY0MGRmY2M4MjAyNzY2YmI4NDNjNDIxNWUyZGJmJnVpZD0mdXVpZD04ODNkOTQ4NTljMzE2MWEyZTIyYjc5ODBlMjFiYjE2Ny02NzI3ZTI2NCZ2PTImYXM9MCZlcz0xNzMxOTE1Mzkz/HNGGMPP360.m3u8
        return "#EXTINF:-1 group-title=\"" + groupTitle
                + "\" tvg-id=\"" + tvgId
                + "\" tvg-name=\"" + getTvgName()
                + "\" tvg-logo=\"" + tvgLogo + "\", "
                + title + "\n" + url;
    }
}
