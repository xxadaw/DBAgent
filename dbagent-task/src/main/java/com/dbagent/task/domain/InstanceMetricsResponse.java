package com.dbagent.task.domain;

import ch.qos.logback.core.joran.sanity.Pair;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname InstanceMetricsResponse
 * @Description TODO
 * @Date 2025/8/14 21:37
 * @Created by xxx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstanceMetricsResponse {
    private String instanceId;
    private List<JSONObject> instanceMetrics;
}
