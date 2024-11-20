package io.github.bigmouthcn.m3u8checker.database;

import cn.hutool.core.io.IoUtil;
import io.github.bigmouthcn.m3u8checker.checker.CheckResult;
import io.github.bigmouthcn.m3u8checker.checker.FailType;
import io.github.bigmouthcn.m3u8checker.checker.M3u8;
import io.github.bigmouthcn.m3u8checker.checker.MetaInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Configuration
public class InfoServiceImpl implements InfoService {

    // CREATE TABLE IF NOT EXISTS tvg_info
    //(
    //    id INTEGER PRIMARY KEY AUTOINCREMENT,
    //    tvg_id TEXT,
    //    tvg_name TEXT,
    //    tvg_logo TEXT,
    //    group_title TEXT,
    //    title TEXT,
    //    url TEXT,
    //    status TEXT,
    //    cost_time TEXT,
    //    last_check_time TEXT,
    //    video_width INTEGER,
    //    video_height INTEGER,
    //    video_codec TEXT,
    //    video_fps INTEGER,
    //    audio_codec TEXT,
    //    audio_sample_rate INTEGER
    //);

    private final JdbcTemplate jdbcTemplate;

    public InfoServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTable() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("create_table.sql");
        String sql = IoUtil.read(classPathResource.getInputStream(), Charset.defaultCharset());
        jdbcTemplate.execute(sql);
    }

    @Override
    public List<M3u8> findAll() {
        return jdbcTemplate.query("SELECT * FROM tvg_info ORDER BY id", getM3u8RowMapper());
    }

    private static RowMapper<M3u8> getM3u8RowMapper() {
        return new RowMapper<M3u8>() {
            @Override
            public M3u8 mapRow(ResultSet rs, int rowNum) throws SQLException {
                M3u8 m3u8 = new M3u8();
                m3u8.setId(rs.getInt("id"));
                m3u8.setTvgId(rs.getString("tvg_id"));
                m3u8.setTvgName(rs.getString("tvg_name"));
                m3u8.setTvgLogo(rs.getString("tvg_logo"));
                m3u8.setGroupTitle(rs.getString("group_title"));
                m3u8.setTitle(rs.getString("title"));
                m3u8.setUrl(rs.getString("url"));
                m3u8.setStatus(rs.getString("status"));
                m3u8.setCostTime(rs.getString("cost_time"));
                m3u8.setLastCheckTime(rs.getString("last_check_time"));
                m3u8.setVideoWidth(rs.getInt("video_width"));
                m3u8.setVideoHeight(rs.getInt("video_height"));
                m3u8.setVideoCodec(rs.getString("video_codec"));
                m3u8.setVideoFps(rs.getInt("video_fps"));
                m3u8.setAudioCodec(rs.getString("audio_codec"));
                m3u8.setAudioSampleRate(rs.getInt("audio_sample_rate"));
                return m3u8;
            }
        };
    }

    @Override
    public void save(M3u8 m3u8) {
        jdbcTemplate.update("INSERT INTO tvg_info (tvg_id, tvg_name, tvg_logo, group_title, title, url, status, cost_time, last_check_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                m3u8.getTvgId(), m3u8.getTvgName(), m3u8.getTvgLogo(), m3u8.getGroupTitle(), m3u8.getTitle(), m3u8.getUrl(), m3u8.getStatus(), m3u8.getCostTime(), m3u8.getLastCheckTime());
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM tvg_info WHERE id = ?", id);
    }

    @Override
    public void update(M3u8 m3u8) {
        jdbcTemplate.update("UPDATE tvg_info SET tvg_id = ?, tvg_name = ?, tvg_logo = ?, group_title = ?, title = ?, url = ?, status = ?, cost_time = ?, last_check_time = ? WHERE id = ?",
                m3u8.getTvgId(), m3u8.getTvgName(), m3u8.getTvgLogo(), m3u8.getGroupTitle(), m3u8.getTitle(), m3u8.getUrl(), m3u8.getStatus(), m3u8.getCostTime(), m3u8.getLastCheckTime(), m3u8.getId());
    }

    @Override
    public void updateJustInfo(M3u8 m3u8) {
        jdbcTemplate.update("UPDATE tvg_info SET tvg_id = ?, tvg_name = ?, tvg_logo = ?, group_title = ?, title = ?, url = ? WHERE id = ?",
                m3u8.getTvgId(), m3u8.getTvgName(), m3u8.getTvgLogo(), m3u8.getGroupTitle(), m3u8.getTitle(), m3u8.getUrl(), m3u8.getId());
    }

    @Override
    public M3u8 findById(Integer id) {
        List<M3u8> query = jdbcTemplate.query("SELECT * FROM tvg_info WHERE id = ?", new Object[]{id}, getM3u8RowMapper());
        return CollectionUtils.isNotEmpty(query) ? query.get(0) : null;
    }

    @Override
    public List<M3u8> findByTvgName(String tvgName) {
        return jdbcTemplate.query("SELECT * FROM tvg_info WHERE tvg_name = ?", new Object[]{tvgName}, getM3u8RowMapper());
    }

    @Override
    public void updateCheckResult(M3u8 m3u8, CheckResult checkResult, FailType failType) {
        Integer id = m3u8.getId();
        M3u8 exists = Objects.nonNull(id) ? findById(id) : null;
        String status = Objects.nonNull(failType) ? failType.name() : "";
        String costTime = Objects.nonNull(checkResult) ? String.valueOf(checkResult.getDuration()) : "";
        String lastCheckTime = String.valueOf(System.currentTimeMillis());
        if (exists != null) {
            // 如果存在，那么说明是对数据库进行检查
            jdbcTemplate.update("UPDATE tvg_info SET status = ?, cost_time = ?, last_check_time = ? WHERE id = ?",
                    status, costTime, lastCheckTime, id);
        } else {
            // 如果不存在，那么是新的数据。
            // 先按名称和URL查找，判断存在则更新，否则插入新数据。
            List<M3u8> m3u8s = findByTvgNameAndUrl(m3u8.getTvgName(), m3u8.getUrl());
            if (m3u8s.size() > 0) {
                // 理论上不存在多条数据的
                jdbcTemplate.update("UPDATE tvg_info SET status = ?, cost_time = ?, last_check_time = ? WHERE id = ?",
                        status, costTime, lastCheckTime, m3u8s.get(0).getId());
            } else {
                m3u8.setStatus(status)
                        .setCostTime(costTime)
                        .setLastCheckTime(lastCheckTime);
                this.save(m3u8);
            }
        }
    }

    @Override
    public void updateMetaInfo(M3u8 m3u8, MetaInfo metaInfo) {
        Integer id = m3u8.getId();
        M3u8 exists = Objects.nonNull(id) ? findById(id) : null;
        if (exists != null) {
            MetaInfo.VideoInfo videoInfo = metaInfo.getVideoInfo();
            MetaInfo.AudioInfo audioInfo = metaInfo.getAudioInfo();
            jdbcTemplate.update("UPDATE tvg_info SET video_width = ?, video_height = ?, video_codec = ?, video_fps = ?, audio_codec = ?, audio_sample_rate = ? WHERE id = ?",
                    videoInfo.getWidth(), videoInfo.getHeight(), videoInfo.getCodec(), videoInfo.getFps(), audioInfo.getCodec(), audioInfo.getSampleRate(), id);
        }
    }

    @Override
    public Set<String> groupByGroupName() {
        return new HashSet<>(jdbcTemplate.query("SELECT group_title FROM tvg_info GROUP BY group_title", new Object[]{}, (rs, rowNum) -> rs.getString("group_title")));
    }

    private List<M3u8> findByTvgNameAndUrl(String tvgName, String url) {
        return jdbcTemplate.query("SELECT * FROM tvg_info WHERE tvg_name = ? AND url = ?", new Object[]{tvgName, url}, getM3u8RowMapper());
    }
}
