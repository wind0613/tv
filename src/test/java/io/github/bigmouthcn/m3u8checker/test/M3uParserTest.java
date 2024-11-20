package io.github.bigmouthcn.m3u8checker.test;

import io.github.bigmouthcn.m3u8checker.parser.FileM3uParser;
import io.github.bigmouthcn.m3u8checker.checker.M3u8;
import org.junit.Test;

import java.util.List;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
public class M3uParserTest {

    @Test
    public void parseFile() {
        String file = "/Users/huxiao/Downloads/ipv6.m3u";
        List<M3u8> m3u8List = new FileM3uParser().parse(file);
        for (M3u8 m3u8 : m3u8List) {
            System.out.println(m3u8);
        }
    }
}
