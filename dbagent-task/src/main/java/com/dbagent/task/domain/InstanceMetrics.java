package com.dbagent.task.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Classname InstanceMetrics
 * @Description TODO
 * @Date 2025/8/12 22:08
 * @Created by xxx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstanceMetrics {
    private Long id;
    private String instanceId;
    private String metrics;
    private String sysMetrics;
    private LocalDateTime collectTime;
}
