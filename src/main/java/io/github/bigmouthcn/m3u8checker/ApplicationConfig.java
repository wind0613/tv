package io.github.bigmouthcn.m3u8checker;

import io.github.bigmouthcn.m3u8checker.checker.M3u8;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Allen Hu
 * @date 2024/11/19
 */
@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {

    private String ffmpegPath = "ffmpeg";

    /**
     * 频道组的映射。
     * key 是频道组名称，value 是符合正则表达式的字符串集合。
     */
    private Map<String, Set<String>> groupMapping = new HashMap<>();

    /**
     * 频道名称的映射。
     * key 是频道名称，value 是符合正则表达式的字符串集合。
     */
    private Map<String, Set<String>> titleMapping = new HashMap<>();

    /**
     * 指定频道名称对应的频道组
     */
    private Map<String, Set<String>> title2GroupMapping = new HashMap<>();

    /**
     * 需要删除的频道名称中包含的字符
     */
    private Set<String> removeChars = new HashSet<>();

    public void updateM3u8Titles(M3u8 m3u8) {
        String groupTitle = m3u8.getGroupTitle();
        String title = m3u8.getTitle();
        title = matchesTitle(title);
        title = removeChars(title);
        m3u8.setTitle(title);

        groupTitle = matchesGroupTitle(groupTitle);
        groupTitle = matchesTitle2Group(title, groupTitle);
        m3u8.setGroupTitle(groupTitle);
    }

    public String removeChars(String title) {
        for (String removeChar : removeChars) {
            title = title.replace(removeChar, "");
        }
        return title;
    }

    public String matchesGroupTitle(String str) {
        return matches(groupMapping, str, str);
    }

    public String matchesTitle(String str) {
        return matches(titleMapping, str, str);
    }

    public String matchesTitle2Group(String str, String groupTitle) {
        return matches(title2GroupMapping, str, groupTitle);
    }

    public String matches(Map<String, Set<String>> mapping, String str, String defaultStr) {
        for (Map.Entry<String, Set<String>> entry : mapping.entrySet()) {
            if (entry.getValue().stream().anyMatch(str::matches)) {
                return entry.getKey();
            }
        }
        return defaultStr;
    }
}
