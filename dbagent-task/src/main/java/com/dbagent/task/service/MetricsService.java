package com.dbagent.task.service;

import com.dbagent.instance.constant.Result;
import com.dbagent.task.domain.InstanceMetricsRequest;

/**
 * @Classname MetricsService
 * @Description TODO
 * @Date 2025/8/14 21:43
 * @Created by xxx
 */
public interface MetricsService {
    Result list(InstanceMetricsRequest instanceMetricsRequest);
}
