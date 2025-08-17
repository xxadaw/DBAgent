package com.dbagent.task.anomaly.cases;

import com.alibaba.fastjson2.JSONObject;
import com.dbagent.instance.constant.Constant;
import com.dbagent.instance.domain.RequestExecuteSql;
import com.dbagent.instance.service.InstanceService;
import com.dbagent.instance.service.MysqlInstanceService;
import com.dbagent.task.anomaly.DBAnomalyDetector;
import com.dbagent.task.anomaly.domain.AnomalyResult;
import com.dbagent.task.mapper.AnomalyMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname UncommittedTransactionDetector
 * @Description TODO
 * @Date 2025/8/13 23:42
 * @Created by xxx
 */
@Slf4j
@Service
@AllArgsConstructor
public class UncommittedTransactionDetector implements DBAnomalyDetector {

    private final static String SQL = """
            SELECT trx_id, trx_started , trx_query ,trx_mysql_thread_id
            FROM information_schema.innodb_trx
            WHERE TIMESTAMPDIFF(SECOND, trx_started, NOW()) > 60
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
            anomalyMapper.deleteAnomalyByInstanceIdAndType(instanceId, Constant.UNCOMMITTED_TRANSACTION);
            List<JSONObject> result = mysqlInstanceService.executeSelect(requestExecuteSql);
            if (result == null || result.isEmpty()) {
                return;
            }
            log.warn("Time {}, Detected uncommitted transaction: {}", System.currentTimeMillis(), result);
            anomalyMapper.addAnomalys(result.stream()
                    .map(jsonObject -> AnomalyResult.builder()
                            .instanceId(instanceId)
                            .type(Constant.UNCOMMITTED_TRANSACTION)
                            .time(LocalDateTime.now())
                            .detail(jsonObject.toString())
                            .build())
                    .toList());
        } catch (SQLException e) {
            log.error("Error executing SQL query: {}", e.getMessage());
        }
    }
}
