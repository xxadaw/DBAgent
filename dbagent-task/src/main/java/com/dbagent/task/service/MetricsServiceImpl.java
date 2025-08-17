package com.dbagent.task.service;

import com.alibaba.fastjson2.JSONObject;
import com.dbagent.instance.constant.Result;
import com.dbagent.task.domain.InstanceMetrics;
import com.dbagent.task.domain.InstanceMetricsRequest;
import com.dbagent.task.domain.InstanceMetricsResponse;
import com.dbagent.task.mapper.MetricsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname MetricsServiceImpl
 * @Description TODO
 * @Date 2025/8/14 21:43
 * @Created by xxx
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {
    private final MetricsMapper metricsMapper;

    @Override
    public Result list(InstanceMetricsRequest instanceMetricsRequest) {
        if (instanceMetricsRequest.getStartTime() == null || instanceMetricsRequest.getEndTime() == null) {
            instanceMetricsRequest.setStartTime(LocalDateTime.now().minusDays(1));
            instanceMetricsRequest.setEndTime(LocalDateTime.now());
        }
        List<InstanceMetrics> instances = metricsMapper.getMetrics(instanceMetricsRequest);
        log.info("get InstanceId {},Metrics {}", instanceMetricsRequest.getInstanceId(), instances);
        List<JSONObject> list = Lists.newArrayList();
        instances.forEach(instance -> {
            JSONObject metrics = JSONObject.parseObject(instance.getMetrics()
                    .substring(instance.getMetrics().indexOf("{"), instance.getMetrics().lastIndexOf("}") + 1));
            JSONObject sysMetrics = JSONObject.parseObject(instance.getSysMetrics()
                    .substring(instance.getSysMetrics().indexOf("{"), instance.getSysMetrics().lastIndexOf("}") + 1));
            JSONObject instanceMetrics = new JSONObject();
            instanceMetrics.put("metrics", metrics.getString(instanceMetricsRequest.getMetrics()));
            instanceMetrics.put("sysMetrics", sysMetrics.getString(instanceMetricsRequest.getSysMetrics()));
            instanceMetrics.put("collectTime", instance.getCollectTime());
            list.add(instanceMetrics);
        });
        return Result.success(InstanceMetricsResponse.builder()
                .instanceId(instanceMetricsRequest.getInstanceId())
                .instanceMetrics(list)
                .build());
    }
}
