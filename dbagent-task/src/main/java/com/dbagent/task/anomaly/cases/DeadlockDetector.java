package com.dbagent.task.anomaly.cases;

import com.alibaba.fastjson2.JSONObject;
import com.dbagent.common.utils.StringUtils;
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
 * @Classname DeadlockDetector
 * @Description TODO
 * @Date 2025/8/13 22:43
 * @Created by xxx
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeadlockDetector implements DBAnomalyDetector {

    private final MysqlInstanceService mysqlInstanceService;
    private final InstanceService instanceService;

    private final AnomalyMapper anomalyMapper;

    private final String SQL = "SHOW ENGINE INNODB STATUS";


    private static String parseStatus(String status) {
        int idx = status.indexOf("LATEST DETECTED DEADLOCK");
        if (idx == -1) return null;
        String part = status.substring(idx);
        // 截取到 "------------------------" 结束符
        int endIdx = part.indexOf("------------");
        if (endIdx > 0) {
            part = part.substring(0, endIdx);
        }
        return part;
    }


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
            List<JSONObject> result = mysqlInstanceService.executeSelect(requestExecuteSql);
            String status = result.get(0).getString("Status");
            String detail = parseStatus(status);
            if (StringUtils.isEmpty(detail)) {
                return;
            }
            log.warn("Time {}, Detected deadlock: {}", System.currentTimeMillis(), detail);
            AnomalyResult anomalyResult = AnomalyResult.builder()
                    .instanceId(instanceId)
                    .type(Constant.DEADLOCK)
                    .time(LocalDateTime.now())
                    .detail(detail)
                    .build();
            anomalyMapper.addAnomaly(anomalyResult);
        } catch (SQLException e) {
            log.error("Time {}, Error executing SQL query: {}", System.currentTimeMillis(), e.getMessage());
        }
    }
}
