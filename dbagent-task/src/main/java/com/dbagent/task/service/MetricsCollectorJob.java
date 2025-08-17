package com.dbagent.task.service;

import java.util.List;

/**
 * @Classname MetricsCollector
 * @Description TODO
 * @Date 2025/8/12 00:18
 * @Created by xxx
 */
public interface MetricsCollectorJob {
    void collectMetrics();

    void collectSystemMetrics();
}
