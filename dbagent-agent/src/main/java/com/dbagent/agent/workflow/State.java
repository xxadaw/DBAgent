package com.dbagent.agent.workflow;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.openai.internal.chat.Message;
import org.bsc.langgraph4j.langchain4j.serializer.std.ChatMesssageSerializer;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ToolExecutionRequestSerializer;
import java.util.Map;
import java.util.Optional;

/**
 * @Classname State
 * @Description TODO
 * @Date 2025/8/16 17:59
 * @Created by xxx
 */
public class State extends MessagesState<ChatMessage> {
    public static final String DATABASE_KEY = "database";
    public static final String SQL_KEY = "sql";
    public static final String SCHEMA_KEY = "schema";
    public static final String PLAN_KEY = "plan";
    public static final String OPTIMIZED_SQL_KEY = "optimized_sql";
    public static final String SUGGESTION_KEY = "suggestion";
    public static final String RESULT_KEY = "result";
    public State(Map<String, Object> initData) {
        super(initData);
    }
}
