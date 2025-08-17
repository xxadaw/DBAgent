package com.dbagent.agent.workflow;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname ConversationState
 * @Description TODO
 * @Date 2025/8/16 15:25
 * @Created by xxx
 */
public class ConversationState extends AgentState {

    public static final String MESSAGES_KEY = "messages";
    public static final String STEP_COUNT_KEY = "step_count";
    public static final String USER_INTENT_KEY = "user_intent";
    public static final String NEEDS_TOOL_KEY = "needs_tool";
    public static final String TOOL_RESULT_KEY = "tool_result";
    public static final Map<String, Channel<?>> SCHEMA = Map.of(
            MESSAGES_KEY, Channels.appender(ArrayList::new),
            STEP_COUNT_KEY, Channels.base(() -> 0),
            USER_INTENT_KEY, Channels.base(() -> ""),
            NEEDS_TOOL_KEY, Channels.base(() -> false),
            TOOL_RESULT_KEY, Channels.base(() -> "")
    );

    public ConversationState(Map<String, Object> initData) {
        super(initData);
    }

    public List<String> messages() {
        return this.<List<String>>value(MESSAGES_KEY)
                .orElse(List.of());
    }

    public int stepCount() {
        return this.<Integer>value(STEP_COUNT_KEY).orElse(0);
    }

    public String userIntent() {
        return this.<String>value(USER_INTENT_KEY).orElse("");
    }

    public Boolean needsTool() {
        return this.<Boolean>value(NEEDS_TOOL_KEY).orElse(false);
    }

    public String toolResult() {
        return this.<String>value(TOOL_RESULT_KEY).orElse("");
    }
}
