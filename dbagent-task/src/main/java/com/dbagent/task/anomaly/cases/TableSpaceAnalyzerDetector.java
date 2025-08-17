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
 * @Classname TableSpaceAnalyzer
 * @Description TODO
 * @Date 2025/8/14 21:23
 * @Created by xxx
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TableSpaceAnalyzerDetector implements DBAnomalyDetector {
    private static final String SQL = """
            SELECT
                t.table_schema                                                                   AS db_name,
                t.table_name                                                                     AS table_name,
                t.engine                                                                         AS engine,
                t.table_rows                                                                     AS row_count,
                ROUND(t.data_length/1024/1024, 2)                                                AS data_mb,
                ROUND(t.index_length/1024/1024, 2)                                               AS index_mb,
                ROUND((t.data_length+t.index_length)/1024/1024, 2)                               AS total_mb,
                ROUND(t.data_free/1024/1024, 2)                                                  AS free_mb,
                IF((t.data_length + t.index_length + t.data_free) = 0, 0,
                   ROUND(t.data_free / (t.data_length + t.index_length + t.data_free) * 100, 2)) AS fragment_pct,
                t.create_time,
                t.update_time
            FROM information_schema.tables t
            WHERE t.table_schema NOT IN ('mysql','performance_schema','information_schema','sys')
              AND t.engine IS NOT NULL
            ORDER BY fragment_pct DESC, total_mb DESC
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
            anomalyMapper.deleteAnomalyByInstanceIdAndType(instanceId, Constant.TABLE_SPACE);
            List<JSONObject> result = mysqlInstanceService.executeSelect(requestExecuteSql);
            if (result == null || result.isEmpty()) {
                return;
            }
            // log.warn("Time {},InstanceID {}, Detected table space: {}", System.currentTimeMillis(),instanceId, result);
            anomalyMapper.addAnomalys(result.stream()
                    .map(jsonObject -> AnomalyResult.builder()
                            .instanceId(instanceId)
                            .type(Constant.TABLE_SPACE)
                            .time(LocalDateTime.now())
                            .detail(jsonObject.toString())
                            .build()
                    ).toList());
        } catch (SQLException e) {
            log.error("Error executing SQL Table Space query: {}", e.getMessage());
        }
    }
}
