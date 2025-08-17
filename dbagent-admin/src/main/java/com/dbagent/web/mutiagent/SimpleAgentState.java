package com.dbagent.web.mutiagent;

import lombok.Getter;
import org.bsc.langgraph4j.state.AgentState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname AgentState
 * @Description TODO
 * @Date 2025/8/16 00:03
 * @Created by xxx
 */
@Getter
public class SimpleAgentState extends AgentState {

    public static final String MESSAGES_KEY = "messages";

    private final List<String> messages = new ArrayList<>();
    private final Map<String, Object> data;

    public SimpleAgentState(Map<String, Object> initialData) {
        super(initialData);
        this.data = initialData;
        if (initialData.containsKey(MESSAGES_KEY)) {
            Object msg = initialData.get(MESSAGES_KEY);
            if (msg instanceof String) messages.add((String) msg);
        }
    }

    public List<String> messages() {
        return messages;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void addMessage(String msg) {
        messages.add(msg);
    }

}

