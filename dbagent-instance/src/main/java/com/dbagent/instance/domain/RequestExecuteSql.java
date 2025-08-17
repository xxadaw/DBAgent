package com.dbagent.instance.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname RequestExecuteSql
 * @Description TODO
 * @Date 2025/8/10 22:40
 * @Created by xxx
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestExecuteSql {
    private String instanceId;
    private String databaseName;
    private String sql;
}
