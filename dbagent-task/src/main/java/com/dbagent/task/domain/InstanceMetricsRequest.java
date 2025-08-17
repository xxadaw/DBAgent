package com.dbagent.task.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Classname InstanceMetricsRequest
 * @Description TODO
 * @Date 2025/8/14 21:35
 * @Created by xxx
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstanceMetricsRequest {
    private String instanceId;
    private String metrics;
    private String sysMetrics;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
