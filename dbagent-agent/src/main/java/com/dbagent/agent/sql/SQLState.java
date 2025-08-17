package com.dbagent.agent.sql;

/**
 * @Classname SQLState
 * @Description TODO
 * @Date 2025/8/17 13:07
 * @Created by xxx
 */

import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Classname SQLState
 * @Description TODO
 * @Date 2025/8/17 12:04
 * @Created by xxx
 */
public class SQLState extends AgentState {
    public static final String INSTANCE_KEY = "instance_id";
    public static final String DATABASEINFO_KEY = "database_info";
    public static final String SQL_KEY = "sql";
    public static final String SCHEMA_KEY = "schema";
    public static final String PLAN_KEY = "plan";
    public static final String OPTIMIZED_SQL_KEY = "optimized_sql";
    public static final String SUGGESTION_KEY = "suggestion";
    public static final String RESULT_KEY = "result";
    public static final Map<String, Channel<?>> SCHEMA = Map.of(
            INSTANCE_KEY,Channels.base(()->""),
            DATABASEINFO_KEY,Channels.base(()->""),
            SQL_KEY,Channels.base(()->""),
            SCHEMA_KEY,Channels.base(()->""),
            PLAN_KEY,Channels.base(()->""),
            SUGGESTION_KEY,Channels.base(()->""),
            RESULT_KEY,Channels.base(()->"")
    );
    
    
    
    public SQLState(Map<String, Object> initData) {
        super(initData);
    }

    public Optional<String> instanceId() {
        return this.value(INSTANCE_KEY);
    }

    public Optional<String> databaseInfo() {
        return this.value(DATABASEINFO_KEY);
    }

    public Optional<String> sql() {
        return this.value(SQL_KEY);
    }

    public Optional<String> schema() {
        return this.value(SCHEMA_KEY);
    }

    public Optional<String> plan() {
        return this.value(PLAN_KEY);
    }

    public Optional<String> optimizedSQL() {
        return this.value(OPTIMIZED_SQL_KEY);
    }

    public Optional<List<String>> suggestion() {
        return this.value(SUGGESTION_KEY);
    }

    public Optional<String> result() {
        return this.value(RESULT_KEY);
    }
}