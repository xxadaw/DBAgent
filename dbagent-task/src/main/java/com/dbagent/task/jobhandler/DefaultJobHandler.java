package com.dbagent.task.jobhandler;

import com.dbagent.task.anomaly.cases.DeadlockDetector;
import com.dbagent.task.anomaly.cases.SlowQueryDetector;
import com.dbagent.task.anomaly.cases.TableSpaceAnalyzerDetector;
import com.dbagent.task.anomaly.cases.UncommittedTransactionDetector;
import com.dbagent.task.domain.InstanceMetrics;
import com.dbagent.task.service.MessagingService;
import com.dbagent.task.service.MetricsCollectorJob;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @Classname DefaultJobHandler
 * @Description TODO
 * @Date 2025/8/12 01:05
 * @Created by xxx
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class DefaultJobHandler {
    private final MetricsCollectorJob metricsCollectorJob;
    private final MessagingService messagingService;


    private final DeadlockDetector deadlockDetector;
    private final UncommittedTransactionDetector uncommittedTransactionDetector;
    private final SlowQueryDetector slowQueryDetector;
    private final TableSpaceAnalyzerDetector tableSpaceAnalyzer;

    @XxlJob("MetricsCollectorJob")
    public void metricsCollectorJob() {
        log.info("开始执行指标采集任务...");
        metricsCollectorJob.collectMetrics();
    }

    @XxlJob("SystemMetricsCollectorJob")
    public void systemMetricsCollectorJob() {
        log.info("开始执行系统指标采集任务...");
        metricsCollectorJob.collectSystemMetrics();
    }

    @XxlJob("DeadLockDetectorJob")
    public void deadLockDetectorJob() {
        log.info("开始执行死锁检测任务...");
        deadlockDetector.detect();
    }

    @XxlJob("UncommittedTransactionDetectorJob")
    public void uncommittedTransactionDetectorJob() {
        log.info("开始执行未提交事务检测任务...");
        uncommittedTransactionDetector.detect();
    }

    @XxlJob("SlowQueryDetectorJob")
    public void slowQueryDetectorJob() {
        log.info("开始执行慢查询检测任务...");
        slowQueryDetector.detect();
    }

    @XxlJob("TableSpaceAnalyzerJob")
    public void tableSpaceAnalyzerJob() {
        log.info("开始执行表空间检测任务...");
        tableSpaceAnalyzer.detect();
    }


    @XxlJob("test")
    public void test() throws IOException {
        log.info("开始执行测试任务...");
        InstanceMetrics msg = InstanceMetrics.builder()
                .id(1L)
                .instanceId("1")
                .metrics("1")
                .sysMetrics("1")
                .build();
        messagingService.sendMetricsMessage(msg);
    }
}
