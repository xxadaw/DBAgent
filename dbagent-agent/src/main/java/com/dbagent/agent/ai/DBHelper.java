package com.dbagent.agent.ai;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname DBHelper
 * @Description TODO
 * @Date 2025/8/9 11:24
 * @Created by xxx
 */
@Slf4j
@Service
public class DBHelper {

    private final String SYSTEM_PROMPT = """
            你的名字是DBHelper,是一个数据库智能助手
            """;
    @Resource
    private ChatModel qwenChatModel;

    public String chat(String input) {
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_PROMPT);
        UserMessage userMessage = UserMessage.from(input);
        ChatResponse chatResponse = qwenChatModel.chat(systemMessage, userMessage);
        log.info("AI 输出:{}", chatResponse);
        return chatResponse.aiMessage().text();
    }

    public String chat(UserMessage userMessage) {
        ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        log.info("AI 输出:{}", chatResponse);
        return chatResponse.aiMessage().text();
    }
}
