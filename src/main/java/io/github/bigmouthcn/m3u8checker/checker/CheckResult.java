package io.github.bigmouthcn.m3u8checker.checker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Allen Hu
 * @date 2024/11/15
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CheckResult {

    private long startTime;
    private long endTime;

    public long getDuration() {
        return endTime - startTime;
    }
}
