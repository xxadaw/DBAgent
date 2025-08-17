package com.dbagent.agent.ai;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Classname DBHelperServiceFactory
 * @Description TODO
 * @Date 2025/8/9 13:03
 * @Created by xxx
 */
@Configuration
public class DBHelperServiceFactory {

    @Resource
    private ChatModel myQwenChatModel;

    @Resource
    private StreamingChatModel myQwenStreamingChatModel;


    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private List<Object> tools;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Bean
    public DBHelperService dbHelperService() {
        return AiServices.builder(DBHelperService.class)
                .chatModel(myQwenChatModel)
                .streamingChatModel(myQwenStreamingChatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(contentRetriever)
                .tools(tools)
                .toolProvider(mcpToolProvider)
                .build();
    }
}
