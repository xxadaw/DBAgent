package com.dbagent.instance.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname Instance
 * @Description TODO
 * @Date 2025/8/10 16:46
 * @Created by xxx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',
 * instance_id   VARCHAR(64)  NOT NULL UNIQUE COMMENT '实例唯一ID（系统生成）',
 * user_id       BIGINT       NOT NULL COMMENT '绑定的用户ID',
 * instance_type VARCHAR(64)  NOT NULL DEFAULT 'mysql' COMMENT '实例类型：mysql/oracle/sqlserver/postgresql/',
 * name          VARCHAR(128) NOT NULL COMMENT '实例名称（用户自定义）',
 * host          VARCHAR(128) NOT NULL COMMENT '数据库主机地址',
 * port          INT          NOT NULL DEFAULT 3306 COMMENT '数据库端口',
 * database_name VARCHAR(128) NOT NULL COMMENT '默认数据库名',
 * username      VARCHAR(64)  NOT NULL COMMENT '数据库用户名',
 * password_enc  TEXT         NOT NULL COMMENT '加密后的数据库密码',
 * create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 * update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
 */
@Builder
public class Instance {
    protected Long id;
    protected String instanceId;
    protected Long userId;
    protected String instanceType;
    protected String name;
    protected String host;
    protected Integer port;
    protected String databaseName;
    protected String username;
    protected String passwordEnc;
    protected String version;
    protected Integer status;
    protected String createTime;
    protected String updateTime;
    protected String text;

    public String toString() {
        return "Instance{" +
                "id=" + id +
                ", instanceId='" + instanceId + '\'' +
                ", userId=" + userId +
                ", instanceType='" + instanceType + '\'' +
                ", name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", databaseName='" + databaseName + '\'' +
                ", username='" + username + '\'' +
                ", passwordEnc='" + passwordEnc + '\'' +
                ", version='" + version + '\'' +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
