package com.dbagent.instance.service;

import com.alibaba.fastjson2.JSONObject;
import com.dbagent.instance.domain.RequestExecuteSql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Classname MysqlInstanceService
 * @Description TODO
 * @Date 2025/8/10 21:52
 * @Created by xxx
 */
public interface MysqlInstanceService {
    Object executeSql(RequestExecuteSql requestExecuteSql) throws SQLException;

    List<JSONObject> executeSelect(RequestExecuteSql requestExecuteSql) throws SQLException;

    int executeInsert(RequestExecuteSql requestExecuteSql) throws SQLException;

    int executeUpdate(RequestExecuteSql requestExecuteSql) throws SQLException;

    int executeDelete(RequestExecuteSql requestExecuteSql) throws SQLException;

    boolean executeDDL(RequestExecuteSql requestExecuteSql) throws SQLException;
}
