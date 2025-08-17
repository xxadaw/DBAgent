package com.dbagent.instance.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname MySqlInstanceConfig
 * @Description TODO
 * @Date 2025/8/10 21:47
 * @Created by xxx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MySqlInstanceConfig {
    private String instanceId;
    private String host;
    private Integer port;
    private String databaseName;
    private String username;
    private String password;
    private String version;
}
