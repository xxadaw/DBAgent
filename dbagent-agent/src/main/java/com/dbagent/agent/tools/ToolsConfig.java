package com.dbagent.agent.tools;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Classname ToolsConfig
 * @Description TODO
 * @Date 2025/8/9 20:19
 * @Created by xxx
 */
@Configuration
public class ToolsConfig {

    @Resource
    private InterviewQuestionTool interviewQuestionTool;

    @Bean
    public List<Object> tools() {
        return List.of(interviewQuestionTool);
    }
}
