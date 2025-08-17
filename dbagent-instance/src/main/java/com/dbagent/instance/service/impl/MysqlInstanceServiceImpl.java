package com.dbagent.instance.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.dbagent.common.utils.StringUtils;
import com.dbagent.instance.domain.Instance;
import com.dbagent.instance.domain.MySqlInstanceConfig;
import com.dbagent.instance.domain.RequestExecuteSql;
import com.dbagent.instance.manager.UserDbConnectionPoolManager;
import com.dbagent.instance.service.InstanceService;
import com.dbagent.instance.service.MysqlInstanceService;
import com.dbagent.instance.utils.MySqlUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * @Classname MysqlInstanceServiceImpl
 * @Description TODO
 * @Date 2025/8/10 21:52
 * @Created by xxx
 */
@Service
@Slf4j
public class MysqlInstanceServiceImpl implements MysqlInstanceService {
    @Autowired
    private UserDbConnectionPoolManager userPoolManager;
    @Autowired
    private InstanceService instanceService;


    private MySqlInstanceConfig getMySqlInstanceConfig(RequestExecuteSql requestExecuteSql) {
        Instance instance = instanceService.getInstanceByInstanceId(requestExecuteSql.getInstanceId());
        if (instance == null) {
            log.error("executeQuerySql instanceId: {} not found", requestExecuteSql.getInstanceId());
            return null;
        }
        MySqlInstanceConfig config = MySqlInstanceConfig.builder()
                .instanceId(instance.getInstanceId())
                .host(instance.getHost())
                .port(instance.getPort())
                .databaseName(
                        StringUtils.isEmpty(requestExecuteSql.getDatabaseName())
                                ? instance.getDatabaseName()
                                : requestExecuteSql.getDatabaseName())
                .username(instance.getUsername())
                .password(MySqlUtil.aesDecrypt(instance.getPasswordEnc(), MySqlUtil.DEFAULT_KEY))
                .build();
        if (MySqlUtil.ping(config) == 0) {
            log.error("executeQuerySql instanceId: {} database connection failed", requestExecuteSql.getInstanceId());
            return null;
        }
        return config;
    }

    public Connection getConnection(RequestExecuteSql requestExecuteSql) throws SQLException {
        MySqlInstanceConfig config = getMySqlInstanceConfig(requestExecuteSql);
        if (config == null) {
            return null;
        }
        HikariDataSource ds = userPoolManager.getOrCreatePool(config);
        return ds.getConnection();
    }

    @Override
    public Object executeSql(RequestExecuteSql requestExecuteSql) throws SQLException {
        log.info("executeSql request: {}", requestExecuteSql);
        // 1. 基础安全检查
        String trimmed = requestExecuteSql.getSql().trim();
        String upperSql = trimmed.toUpperCase(Locale.ROOT);
        if (upperSql.startsWith("SELECT")) {
            return executeSelect(requestExecuteSql);
        } else if (upperSql.startsWith("INSERT")) {
            return executeInsert(requestExecuteSql);
        } else if (upperSql.startsWith("UPDATE")) {
            return executeUpdate(requestExecuteSql);
        } else if (upperSql.startsWith("DELETE")) {
            return executeDelete(requestExecuteSql);
        } else {
            return executeDDL(requestExecuteSql);
        }
    }


    /**
     * SELECT 查询
     */
    @Override
    public List<JSONObject> executeSelect(RequestExecuteSql requestExecuteSql) throws SQLException {
        String sql = requestExecuteSql.getSql();
        if (StringUtils.isEmpty(sql)) {
            return Collections.emptyList();
        }
        log.info("executeSelect:{}", sql);
        try (Connection conn = getConnection(requestExecuteSql);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<JSONObject> results = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                JSONObject json = new JSONObject(row);
                results.add(json);
            }
            log.info("Results: {}", results);
            return results;
        }
    }

    /**
     * INSERT 执行
     */
    @Override
    public int executeInsert(RequestExecuteSql requestExecuteSql) throws SQLException {
        String sql = requestExecuteSql.getSql();
        if (sql == null || StringUtils.isEmpty(sql)) {
            return 0;
        }
        try (Connection conn = getConnection(requestExecuteSql);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeUpdate();
        }
    }

    /**
     * UPDATE 执行
     */
    @Override
    public int executeUpdate(RequestExecuteSql requestExecuteSql) throws SQLException {
        String sql = requestExecuteSql.getSql();
        if (StringUtils.isEmpty(sql)) {
            return 0;
        }
        try (Connection conn = getConnection(requestExecuteSql);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeUpdate();
        }
    }

    /**
     * DELETE 执行
     */
    @Override
    public int executeDelete(RequestExecuteSql requestExecuteSql) throws SQLException {
        String sql = requestExecuteSql.getSql();
        if (StringUtils.isEmpty(sql)) {
            return 0;
        }
        try (Connection conn = getConnection(requestExecuteSql);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeUpdate();
        }
    }

    @Override
    public boolean executeDDL(RequestExecuteSql requestExecuteSql) throws SQLException {
        String sql = requestExecuteSql.getSql();
        if (StringUtils.isEmpty(sql)) {
            return false;
        }
        try (Connection conn = getConnection(requestExecuteSql);
             Statement stmt = conn.createStatement()) {
            return stmt.execute(sql);
        }
    }
}
