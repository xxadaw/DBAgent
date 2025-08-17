package com.dbagent.agent.sql;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ChatMessage;
import org.bsc.langgraph4j.langchain4j.serializer.std.ChatMesssageSerializer;
import org.bsc.langgraph4j.langchain4j.serializer.std.ToolExecutionRequestSerializer;
import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.bsc.langgraph4j.serializer.std.ObjectStreamStateSerializer;

import java.util.Map;
/**
 * 状态序列化程序
 *
 * @author xxx
 * @date 2025/08/17 13:03
 */
public class SQLStateSerializer extends ObjectStreamStateSerializer<SQLState> {

    public SQLStateSerializer() {
        super(SQLState::new);

        mapper().register(ToolExecutionRequest.class, new ToolExecutionRequestSerializer());
        mapper().register(ChatMessage.class, new ChatMesssageSerializer());
    }
}