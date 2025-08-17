package com.dbagent.agent.sql;

import com.dbagent.instance.domain.Instance;
import com.dbagent.instance.domain.RequestExecuteSql;
import com.dbagent.instance.domain.RequestInstance;
import com.dbagent.instance.service.InstanceService;
import com.dbagent.instance.service.MysqlInstanceService;
import org.apache.commons.compress.utils.Lists;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Classname GatherInfoNode
 * @Description TODO
 * @Date 2025/8/17 13:03
 * @Created by xxx
 */
@Component
public class GatherInfoNode implements NodeAction<SQLState> {

    private static final List<String> SQL = List.of(
            "SELECT VERSION();",               // 数据库版本
            "SHOW VARIABLES LIKE 'version%';", //详细版本、协议版本
            "SHOW ENGINES;",                   //支持的存储引擎
            "SELECT @@version_comment;"        // 是否是MySQL、MariaDB、发行信息
    );

    @Autowired
    private MysqlInstanceService mysqlInstanceService;

    @Autowired
    private InstanceService instanceService;

    @Override
    public Map<String, Object> apply(SQLState sqlState) throws Exception {
        String instanceId = sqlState.instanceId().orElse("");
        Instance instanceByInstanceId = instanceService.getInstanceByInstanceId(instanceId);
        String text = "";
        if (instanceByInstanceId.getText().isEmpty()) {
            List<String> info = Lists.newArrayList();
            RequestInstance requestInstance = (RequestInstance) instanceByInstanceId;
            for (String sql : SQL) {
                RequestExecuteSql requestExecuteSql = RequestExecuteSql.builder()
                        .instanceId(instanceId)
                        .sql(sql)
                        .build();
                info.add(mysqlInstanceService.executeSelect(requestExecuteSql).toString());
            }
            requestInstance.setInstanceId(info.toString());
            instanceService.updateInstance(requestInstance);
            text = info.toString();
        } else {
            text = instanceByInstanceId.getText();
        }

        return Map.of(

        );

    }
}
