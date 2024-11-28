package io.github.bigmouthcn.m3u8checker.test;

import io.github.bigmouthcn.m3u8checker.Application;
import io.github.bigmouthcn.m3u8checker.ApplicationConfig;
import io.github.bigmouthcn.m3u8checker.checker.*;
import io.github.bigmouthcn.m3u8checker.parser.FileM3uParser;
import io.github.bigmouthcn.m3u8checker.parser.M3uParser;
import org.junit.Test;

import java.util.List;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
public class M3u8CheckerTest {

    private final M3uParser parser = new FileM3uParser();
    private final M3u8Checker checker = new OkHttpM3u8Checker(new ApplicationConfig(), Application.createOkHttpClient());

    @Test
    public void ipv4Normal() {
        try {
            CheckResult result = checker.check("http://222.241.154.37:9901/tsfile/live/23021_1.m3u8");
            System.out.println(result.getDuration());
        } catch (CheckFailException e) {
            printStackTrace(e);
        }
    }

    @Test
    public void ipv4Fail() {
        try {
            checker.check("http://222.241.154.37:9902/tsfile/live/23021_1.m3u8");
        } catch (CheckFailException e) {
            printStackTrace(e);
        }
    }

    @Test
    public void ipv6Success() {
        try {
            checker.check("http://[2409:8087:74d9:21::6]:80/270000001128/9900000503/index.m3u8");
        } catch (CheckFailException e) {
            printStackTrace(e);
        }
    }

    @Test
    public void liveDouyu() {
        try {
            CheckResult checked = checker.check("https://live.iill.top/douyu/5040227");
            System.out.println(checked.getDuration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void parserChecking() {
        String file = "/Users/huxiao/Downloads/ipv6.m3u";
        List<M3u8> m3u8List = new FileM3uParser().parse(file);
        for (M3u8 m3u8 : m3u8List) {
            String url = m3u8.getUrl();
            try {
                CheckResult result = checker.check(url);
                System.out.println(m3u8.getTvgName() + " SUCCESS in " + result.getDuration() + "ms" + " " + url);
            } catch (CheckFailException e) {
                System.out.println(m3u8.getTvgName() + " FAIL for " + e.getFailType());
            }
        }
    }

    public static void printStackTrace(CheckFailException e) {
        System.err.print(e.getFailType());
        e.printStackTrace();
    }
}
