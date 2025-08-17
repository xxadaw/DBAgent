package com.dbagent.agent.config;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Classname AgentConfig
 * @Description TODO
 * @Date 2025/8/9 12:30
 * @Created by xxx
 */
@Configuration
public class AgentConfig {
    @Resource
    private ChatModelListener chatModelListener;

    @Bean
    public ChatModel myQwenChatModel() {
        return QwenChatModel.builder()
                .modelName("qwen-max")
                .apiKey("sk-790278de5b85487299ea5bd91f1bc859")
                .listeners(List.of(chatModelListener))
                .build();
    }

    @Bean
    public StreamingChatModel myQwenStreamingChatModel() {
        return QwenStreamingChatModel.builder()
                .modelName("qwen-max")
                .apiKey("sk-790278de5b85487299ea5bd91f1bc859")
                .listeners(List.of(chatModelListener))
                .build();
    }

//    @Bean
//    public EmbeddingModel qwenEmbeddingModel() {
//        return QwenEmbeddingModel.builder()
//                .modelName("text-embedding-v4")
//                .apiKey("sk-790278de5b85487299ea5bd91f1bc859")
//                .build();
//    }
//
//    @Bean
//    public InMemoryEmbeddingStore<TextSegment> inMemoryEmbeddingStore() {
//        return new InMemoryEmbeddingStore<>();
//    }

}
