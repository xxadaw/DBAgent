package com.dbagent.instance.constant;

/**
 * @Classname Constant
 * @Description TODO
 * @Date 2025/8/10 16:50
 * @Created by xxx
 */
public class Constant {

    // 数据库类型
    public static final String MYSQL = "mysql";
    public static final String ORACLE = "oracle";
    public static final String SQLSERVER = "sqlserver";
    public static final String POSTGRESQL = "postgresql";

    // 数据库连接参数
    public static final String DATABASE_FIX = "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";


    // 异常类型
    public static final String DEADLOCK = "deadlock";
    public static final String UNCOMMITTED_TRANSACTION = "uncommitted_transaction";
    public static final String SLOW_QUERY = "slow_query";
    public static final String TABLE_SPACE = "table_space";

}
