package io.github.bigmouthcn.m3u8checker.parser;

import cn.hutool.core.io.FileUtil;
import io.github.bigmouthcn.m3u8checker.checker.M3u8;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Configuration
public class FileM3uParser implements M3uParser {
    @Override
    public M3uSource getSource() {
        return M3uSource.FILE;
    }

    @Override
    public List<M3u8> parse(String m3uUrl) {
        List<String> lines = FileUtil.readLines(m3uUrl, "UTF-8");
        if (lines != null && !lines.isEmpty()) {
            return parse(lines);
        }
        return null;
    }
}
