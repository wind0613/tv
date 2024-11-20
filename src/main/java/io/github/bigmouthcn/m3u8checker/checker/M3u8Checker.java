package io.github.bigmouthcn.m3u8checker.checker;

public interface M3u8Checker {

    CheckResult check(String url) throws CheckFailException;

    MetaInfo getMetaInfo(String url);
}
