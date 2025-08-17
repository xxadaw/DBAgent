package com.dbagent.task.anomaly.cases;

import com.alibaba.fastjson2.JSONObject;
import com.dbagent.instance.constant.Constant;
import com.dbagent.instance.domain.RequestExecuteSql;
import com.dbagent.instance.service.InstanceService;
import com.dbagent.instance.service.MysqlInstanceService;
import com.dbagent.task.anomaly.DBAnomalyDetector;
import com.dbagent.task.anomaly.domain.AnomalyResult;
import com.dbagent.task.mapper.AnomalyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname SlowQueryDetector
 * @Description TODO
 * @Date 2025/8/14 00:32
 * @Created by xxx
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SlowQueryDetector implements DBAnomalyDetector {
    private final static String SQL_FOR_DIGEST_TEXT = """
            SELECT DIGEST_TEXT                       AS normalized_sql,
            COUNT_STAR                               AS execution_count, -- 执行次数
               -- 聚合统计
            ROUND(SUM_TIMER_WAIT / 1000000000000, 6) AS total_exec_time_sec,
            ROUND(AVG_TIMER_WAIT / 1000000000000, 6) AS avg_exec_time_sec,
            ROUND(MAX_TIMER_WAIT / 1000000000000, 6) AS max_exec_time_sec,
            SUM_ROWS_EXAMINED,
            SUM_ROWS_SENT
            FROM performance_schema.events_statements_summary_by_digest
            WHERE DIGEST_TEXT IS NOT NULL
            AND AVG_TIMER_WAIT > 1000000000000 -- 平均执行时间超过1秒
            ORDER BY avg_exec_time_sec DESC
            LIMIT 10
            """;
    private final static String SQL = """
            SELECT
                -- 计算并按平均执行时间排序 (秒)
                ROUND(AVG(TIMER_WAIT) / 1000000000000, 6) AS avg_exec_sec,
            
                -- 分类键：SQL 模板
                DIGEST_TEXT AS sql_template,
            
                -- 获取该模板下的一个具体 SQL 原文
                ANY_VALUE(SQL_TEXT) AS sample_sql_text,
            
                -- 其他关键指标
                ROUND(MAX(TIMER_WAIT) / 1000000000000, 6) AS max_exec_sec, -- 最大执行时间
                COUNT(*) AS exec_count,                                  -- 该模板的执行次数
                SUM(ROWS_EXAMINED) AS total_rows_examined,               -- 总扫描行数
                SUM(ROWS_SENT) AS total_rows_sent                        -- 总返回行数
            
            FROM
                performance_schema.events_statements_history
            
            WHERE
                EVENT_NAME LIKE 'statement/sql/%'          -- 只关注SQL语句
              AND DIGEST_TEXT IS NOT NULL                -- 确保有模板
              AND SQL_TEXT IS NOT NULL                   -- 确保有SQL文本
              AND TIMER_WAIT IS NOT NULL                 -- 确保时间有效
              #AND TIMER_WAIT > 1000000000000         -- 例如：单次>1秒
            
            GROUP BY
                DIGEST_TEXT
            
            ORDER BY
                AVG(TIMER_WAIT) DESC
            
            LIMIT 100;
            """;

    private final InstanceService instanceService;
    private final MysqlInstanceService mysqlInstanceService;
    private final AnomalyMapper anomalyMapper;

    @Override
    public void detect() {
        instanceService.getInstanceIds().forEach(this::detect);
    }

    @Override
    public void detect(String instanceId) {
        RequestExecuteSql requestExecuteSql = RequestExecuteSql.builder()
                .instanceId(instanceId)
                .sql(SQL)
                .build();
        try {
            anomalyMapper.deleteAnomalyByInstanceIdAndType(instanceId, Constant.SLOW_QUERY);
            List<JSONObject> result = mysqlInstanceService.executeSelect(requestExecuteSql);
            if (result == null || result.isEmpty()) {
                return;
            }
            log.warn("Time {},InstanceID {}, Detected slow query: {}", System.currentTimeMillis(), instanceId, result);
            anomalyMapper.addAnomalys(result.stream()
                    .map(jsonObject -> AnomalyResult.builder()
                            .instanceId(instanceId)
                            .type(Constant.SLOW_QUERY)
                            .time(LocalDateTime.now())
                            .detail(jsonObject.toString())
                            .build())
                    .toList());
        } catch (SQLException e) {
            log.error("Error executing SQL query: {}", e.getMessage());
        }
    }
}
