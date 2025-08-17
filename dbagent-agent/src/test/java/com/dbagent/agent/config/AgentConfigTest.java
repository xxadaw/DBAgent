package com.dbagent.agent.config;

import com.dbagent.agent.ai.Agent;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Classname AgentConfigTest
 * @Description TODO
 * @Date 2025/8/16 13:35
 * @Created by xxx
 */
class AgentConfigTest {


    @Test
    public void test() {
        QwenChatModel build = QwenChatModel.builder()
                .modelName("qwen-max-2025-01-25")
                .apiKey("sk-790278de5b85487299ea5bd91f1bc859")
                .build();
        build.chat("你是谁");
    }

}