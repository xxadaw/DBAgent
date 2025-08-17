package com.dbagent.agent.ai;

import com.dbagent.agent.guardrail.SafeInputGuardrail;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @Classname DBHelperService
 * @Description TODO
 * @Date 2025/8/9 12:59
 * @Created by xxx
 */

@InputGuardrails(SafeInputGuardrail.class)
public interface DBHelperService {
    @SystemMessage(fromResource = "system_prompt.txt")
    String chat(@MemoryId String memoryId, @UserMessage String input);

    @SystemMessage(fromResource = "system_prompt.txt")
    ChatResponse chat(@MemoryId String memoryId, @UserMessage dev.langchain4j.data.message.UserMessage userMessage);

    Report optimizeSql(@MemoryId String memoryId, @UserMessage String input);

    @SystemMessage(fromResource = "system_prompt.txt")
    Flux<String> streamChat(@MemoryId String memoryId, @UserMessage String input);

    //Sql优化建议
    record Report(String sql, List<String> suggestions) {
    }

}
