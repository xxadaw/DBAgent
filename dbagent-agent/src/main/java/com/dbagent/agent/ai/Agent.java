package com.dbagent.agent.ai;

import dev.langchain4j.model.chat.ChatModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname Agent
 * @Description TODO
 * @Date 2025/8/16 13:49
 * @Created by xxx
 */
@Service
@Slf4j
public class Agent {

    @Resource
    private ChatModel myQwenChatModel;

    public String chat(String userMessage) {
        return myQwenChatModel.chat(userMessage);
    }
}
