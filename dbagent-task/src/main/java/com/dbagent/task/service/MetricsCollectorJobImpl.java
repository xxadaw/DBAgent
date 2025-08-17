package com.dbagent.task.service;

import com.alibaba.fastjson2.JSONObject;
import com.dbagent.common.utils.StringUtils;
import com.dbagent.instance.domain.RequestExecuteSql;
import com.dbagent.instance.service.InstanceService;
import com.dbagent.instance.service.MysqlInstanceService;
import com.dbagent.task.domain.InstanceMetrics;
import com.dbagent.task.mapper.MetricsMapper;
import com.dbagent.task.utils.OshiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Classname MetricsCollectorImpl
 * @Description TODO
 * @Date 2025/8/12 00:19
 * @Created by xxx
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsCollectorJobImpl implements MetricsCollectorJob {

    private static final String METRICS_SQL = "SHOW GLOBAL STATUS";

    private final MysqlInstanceService mysqlInstanceService;
    private final InstanceService instanceService;
    private final MetricsMapper metricsMapper;
    private final MessagingService messagingService;
    private final Set<String> metricsForMySQL = new HashSet<>(Arrays.asList(
            "Connections", "Threads_connected",
            "Threads_running",
            "Questions",
            "Slow_queries",
            "Innodb_buffer_pool_read_requests",
            "Innodb_buffer_pool_reads",
            "Innodb_row_lock_time_avg"
    ));
    private final Set<String> metricsForSystem = new HashSet<>(Arrays.asList(
            "cpu_usage",
            "memory_usage",
            "disk_usage",
            "disk_read_iops",
            "disk_write_iops"
    ));
    @Value("${dbagent.instance-id}")
    private String CUR_INSTANCE_ID;

    @Override
    public void collectMetrics() {
        List<String> instanceIds = instanceService.getInstanceIds();
        log.info("Collecting metrics for instance: {}", instanceIds);
        instanceIds.forEach(instanceId -> {
            RequestExecuteSql requestExecuteSql = RequestExecuteSql.builder()
                    .instanceId(instanceId)
                    .sql(METRICS_SQL)
                    .build();
            JSONObject metrics = new JSONObject();
            try {
                List<JSONObject> metricsList = mysqlInstanceService.executeSelect(requestExecuteSql);
                metricsList.forEach(json -> {
                    String metricName = json.getString("Variable_name");
                    String metricValue = json.getString("Value");
                    if (metricsForMySQL.contains(metricName)) {
                        metrics.put(metricName, metricValue);
                    }
                });
                InstanceMetrics instanceMetrics = metricsMapper.getMetricsByInstanceId(instanceId);
                instanceMetrics = instanceMetrics == null
                        || StringUtils.isEmpty(instanceMetrics.getInstanceId())
                        ? InstanceMetrics.builder().instanceId(instanceId).build()
                        : instanceMetrics;
                String sysMetrics = StringUtils.isEmpty(instanceMetrics.getSysMetrics())
                        ? "{}"
                        : instanceMetrics.getSysMetrics();
                instanceMetrics.setMetrics(metrics.toString());
                instanceMetrics.setSysMetrics(sysMetrics);
                instanceMetrics.setCollectTime(LocalDateTime.now());
                metricsMapper.addMetrics(instanceMetrics);
                log.info("获取指标完成:{}", instanceMetrics);
            } catch (SQLException e) {
                log.error("Error executing SQL query: {}", e.getMessage());
            }
        });
    }

    @Override
    public void collectSystemMetrics() {
        JSONObject systemMetrics = new JSONObject();
        double cpuLoad = OshiUtil.getCpuLoad();
        systemMetrics.put("cpu_load", cpuLoad);
        double memoryUsage = OshiUtil.getMemoryUsage();
        systemMetrics.put("memory_usage", memoryUsage);
        InstanceMetrics instanceMetrics = InstanceMetrics.builder()
                .instanceId(CUR_INSTANCE_ID)
                .sysMetrics(systemMetrics.toJSONString())
                .build();
        log.info("InstanceID :{} , System Metrics: {}", CUR_INSTANCE_ID, systemMetrics);
        try {
            messagingService.sendMetricsMessage(instanceMetrics);
        } catch (IOException e) {
            log.error("Error sending metrics message: {}", e.getMessage());
        }
    }
}