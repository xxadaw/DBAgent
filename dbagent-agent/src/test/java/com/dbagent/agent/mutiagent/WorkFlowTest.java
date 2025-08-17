package com.dbagent.agent.mutiagent;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Classname WorkFlowTest
 * @Description TODO
 * @Date 2025/8/16 19:45
 * @Created by xxx
 */
class WorkFlowTest {

    @Test
    void run() throws Exception {
        WorkFlow workFlow = new WorkFlow();
        workFlow.run();
    }

}