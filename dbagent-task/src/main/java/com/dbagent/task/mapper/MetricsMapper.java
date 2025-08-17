package com.dbagent.task.mapper;

import com.dbagent.task.domain.InstanceMetrics;
import com.dbagent.task.domain.InstanceMetricsRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Classname MetricsMapper.xml
 * @Description TODO
 * @Date 2025/8/12 00:56
 * @Created by xxx
 */
@Mapper
public interface MetricsMapper {
    void addMetrics(InstanceMetrics instanceMetrics);

    void addSystemMetrics(String instanceId, String sysMetrics);

    InstanceMetrics getMetricsByInstanceId(String instanceId);

    List<InstanceMetrics> getMetrics(InstanceMetricsRequest instanceMetricsRequest);
}
