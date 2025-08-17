package com.dbagent.web.test;


import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



import static org.junit.jupiter.api.Assertions.*;
/**
 * @Classname TestToolTest
 * @Description TODO
 * @Date 2025/8/15 23:14
 * @Created by xxx
 */
@SpringBootTest
class TestToolTest {
    @Test
    void test() {
        var model = QwenChatModel.builder()
                .modelName("qwen-max")
                .apiKey("sk-790278de5b85487299ea5bd91f1bc859")
                .build();
    }
  
}