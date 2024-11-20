package io.github.bigmouthcn.m3u8checker.parser;

import io.github.bigmouthcn.m3u8checker.checker.M3u8;

import java.util.ArrayList;
import java.util.List;

public interface M3uParser {

    M3uSource getSource();

    List<M3u8> parse(String m3uUrl);

    default List<M3u8> parse(List<String> lines) {
        List<M3u8> entries = new ArrayList<>();
        M3u8 currentEntry = null;
        for (String line : lines) {
            if (line.startsWith("#EXTINF")) {
                currentEntry = new M3u8();
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    currentEntry.setTitle(parts[1]);
                }

                String[] attributes = parts[0].split(" ");
                for (String attribute : attributes) {
                    if (attribute.startsWith("tvg-id=")) {
                        currentEntry.setTvgId(attribute.split("=")[1].replace("\"", ""));
                    } else if (attribute.startsWith("tvg-name=")) {
                        currentEntry.setTvgName(attribute.split("=")[1].replace("\"", ""));
                    } else if (attribute.startsWith("tvg-logo=")) {
                        currentEntry.setTvgLogo(attribute.split("=")[1].replace("\"", ""));
                    } else if (attribute.startsWith("group-title=")) {
                        currentEntry.setGroupTitle(attribute.split("=")[1].replace("\"", ""));
                    }
                }
            } else if (line.startsWith("http") && currentEntry != null) {
                currentEntry.setUrl(line);
                entries.add(currentEntry);
            }
        }
        return entries;
    }
}
