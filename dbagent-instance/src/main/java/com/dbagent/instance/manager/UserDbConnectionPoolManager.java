package com.dbagent.instance.manager;

import com.dbagent.common.utils.StringUtils;
import com.dbagent.instance.domain.MySqlInstanceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

import static com.dbagent.instance.constant.Constant.DATABASE_FIX;

/**
 * @Classname UserDbConnectionPoolManager
 * @Description TODO
 * @Date 2025/8/10 21:46
 * @Created by xxx
 */
@Configuration
public class UserDbConnectionPoolManager {

    private final ConcurrentHashMap<String, HikariDataSource> poolMap = new ConcurrentHashMap<>();

    public HikariDataSource getOrCreatePool(MySqlInstanceConfig config) {
        return poolMap.computeIfAbsent(config.getInstanceId(), id -> createDataSource(config));
    }

    private HikariDataSource createDataSource(MySqlInstanceConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        if (StringUtils.isEmpty(config.getDatabaseName())) {
            hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + DATABASE_FIX);
        } else {
            hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName() + DATABASE_FIX);
        }
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setMinimumIdle(0);
        hikariConfig.setMaximumPoolSize(3); // 按实际调整
        hikariConfig.setConnectionTimeout(5000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setInitializationFailTimeout(5000); // 添加初始化失败超时设置
        hikariConfig.setValidationTimeout(3000); // 添加验证超时设置
        hikariConfig.setLeakDetectionThreshold(60000); // 添加连接泄漏检测
        return new HikariDataSource(hikariConfig);
    }

    public void closePool(String instanceId) {
        HikariDataSource ds = poolMap.remove(instanceId);
        if (ds != null) {
            ds.close();
        }
    }

    public void closeAll() {
        poolMap.values().forEach(HikariDataSource::close);
        poolMap.clear();
    }
}