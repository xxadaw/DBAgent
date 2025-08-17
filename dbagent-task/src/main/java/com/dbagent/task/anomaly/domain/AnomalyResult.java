package com.dbagent.task.anomaly.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Classname AnomalyResult
 * @Description TODO
 * @Date 2025/8/13 22:42
 * @Created by xxx
 */
@Data
@Builder
public class AnomalyResult {
    private Long id;
    private String instanceId;
    private String type;        // deadlock, uncommitted_tx, slow_query, tablespace
    private LocalDateTime time;
    private String detail;
}
