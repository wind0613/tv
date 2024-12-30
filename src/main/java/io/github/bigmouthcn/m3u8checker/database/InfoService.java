package io.github.bigmouthcn.m3u8checker.database;

import io.github.bigmouthcn.m3u8checker.checker.CheckResult;
import io.github.bigmouthcn.m3u8checker.checker.FailType;
import io.github.bigmouthcn.m3u8checker.checker.M3u8;
import io.github.bigmouthcn.m3u8checker.checker.MetaInfo;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface InfoService {

    void createTable() throws IOException;

    List<M3u8> findAll();

    void insert(M3u8 m3u8);

    void delete(Integer id);

    void update(M3u8 m3u8);

    void updateJustInfo(M3u8 m3u8);

    M3u8 findById(Integer id);

    List<M3u8> findByTvgName(String tvgName);

    void updateCheckResult(M3u8 m3u8, CheckResult checkResult, FailType failType);

    void updateMetaInfo(M3u8 m3u8, MetaInfo metaInfo);

    Set<String> groupByGroupName();
}
