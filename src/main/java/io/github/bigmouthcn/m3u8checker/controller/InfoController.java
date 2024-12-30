package io.github.bigmouthcn.m3u8checker.controller;

import io.github.bigmouthcn.m3u8checker.ApplicationConfig;
import io.github.bigmouthcn.m3u8checker.checker.*;
import io.github.bigmouthcn.m3u8checker.database.InfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@RestController
public class InfoController {

    private final InfoService infoService;
    private final M3u8Checker m3u8Checker;
    private final ApplicationConfig applicationConfig;

    public InfoController(InfoService infoService, M3u8Checker m3u8Checker, ApplicationConfig applicationConfig) {
        this.infoService = infoService;
        this.m3u8Checker = m3u8Checker;
        this.applicationConfig = applicationConfig;
    }

    @GetMapping("/")
    public ResponseEntity<Object> index() {
        return ResponseEntity.ok("Hello, Welcome use M3U8 Checker project. If you have any question, please contact me by email: javahuxiao@gmail.com");
    }

    @GetMapping("/list")
    public ResponseEntity<Object> list(String status) {
        List<M3u8> m3u8List = infoService.findAll();
        m3u8List.removeIf(m3u8 -> StringUtils.equals(M3u8.STATUS_DELETED, m3u8.getStatus()));
        if (StringUtils.equals("OK", status)) {
            m3u8List.removeIf(m3u8 -> StringUtils.isNotBlank(m3u8.getStatus()));
        } else if (StringUtils.isNotBlank(status)) {
            m3u8List.removeIf(m3u8 -> !StringUtils.equals(status, m3u8.getStatus()));
        }
        return ResponseEntity.ok(m3u8List);
    }

    @GetMapping("/groupNameList")
    public ResponseEntity<Object> groupNameList() {
        return ResponseEntity.ok(infoService.groupByGroupName());
    }

    @PostMapping("/update")
    public ResponseEntity<Object> update(@RequestBody M3u8 entity) {
        if (entity.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        infoService.updateJustInfo(entity);
        return ResponseEntity.ok(entity);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        infoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/m3u8/check")
    public ResponseEntity<Object> check(@RequestParam String url) {
        try {
            CheckResult checked = m3u8Checker.check(url);
            MetaInfo metaInfo = m3u8Checker.getMetaInfo(url);
            return ResponseEntity.ok("OK - cost:" + checked.getDuration() + "\n" + metaInfo);
        } catch (CheckFailException e) {
            return ResponseEntity.badRequest().body(e.getFailType());
        }
    }

    /**
     * @param justOk 1 表示仅显示OK的，其他显示所有
     * @return m3u file
     */
    @GetMapping("/m3u/list")
    public ResponseEntity<Object> list(@RequestParam(required = false) String justOk,
                                       @RequestParam(required = false) String withoutRepeat) {
        StringBuilder m3u8Res = new StringBuilder();
        m3u8Res.append("#EXTM3U").append("\n");

        List<M3u8> m3u8List = infoService.findAll();

        // 如果justOk为1，那么仅显示OK的
        if (StringUtils.equals("1", justOk)) {
            m3u8List.removeIf(m3u8 -> !m3u8.isOk());
        }

        // 如果有相同名称的，那么仅保留视频高度最大的
        if (StringUtils.equals("1", withoutRepeat)) {
            Map<String, Integer> maximum = new HashMap<>(m3u8List.size());
            m3u8List.removeIf(m3u8 -> {
                String key = m3u8.getTitle();
                Integer height = maximum.get(key);
                if (height == null) {
                    maximum.put(key, m3u8.getVideoHeight());
                    return false;
                } else {
                    return m3u8.getVideoHeight() < height;
                }
            });
        }

        // 转换成 m3u8 文件格式
        for (M3u8 m3u8 : m3u8List) {
            String line = m3u8.convert2M3u8Line();
            m3u8Res.append(line).append("\n");
        }

        return ResponseEntity.ok(m3u8Res);
    }

    @PostMapping("/updateTvGroupAndTitle")
    public ResponseEntity<Object> updateTvGroupAndTitle() {
        List<M3u8> m3u8List = infoService.findAll();
        for (M3u8 m3u8 : m3u8List) {
            applicationConfig.updateM3u8Titles(m3u8);
            infoService.updateJustInfo(m3u8);
        }
        return ResponseEntity.ok(m3u8List.size());
    }
}
