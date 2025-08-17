package com.dbagent.agent.workflow;

import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.state.AgentState;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Classname GreeterNodeAction
 * @Description TODO
 * @Date 2025/8/16 15:22
 * @Created by xxx
 */
@Slf4j
@Component
public class GreeterNodeAction implements NodeAction<ConversationState> {

    @Override
    public Map<String, Object> apply(ConversationState conversationState) throws Exception {
        System.out.println("GreeterNodeAction:");
        System.out.println("    Current state.toString(): " + conversationState.toString());

        String outputMessage = "Hi, this is greeter.";
        System.out.println("    Output message: " + outputMessage);
        System.out.println();

        return Map.of(ConversationState.MESSAGES_KEY, outputMessage);
    }
}

/**
 * 处理器节点动作
 *
 * @author xxx
 * @date 2025/08/16 15:35
 */
class ProcessorNodeAction implements NodeAction<ConversationState> {
    @Override
    public Map<String, Object> apply(ConversationState state) {
        System.out.println("ProcessorNodeAction:");
        System.out.println("    Current state.toString(): " + state.toString());
        String outputMessage = "I am processor, I've processed your request.";
        System.out.println("    Output message: " + outputMessage);
        System.out.println("    Step count: " + (state.stepCount() + 1));
        System.out.println();

        return Map.of(
                ConversationState.MESSAGES_KEY,   outputMessage,
                ConversationState.STEP_COUNT_KEY, state.stepCount() + 1
        );
    }
}

/**
 * 响应节点操作
 *
 * @author xxx
 * @date 2025/08/16 15:44
 */
class ResponderNodeAction implements NodeAction<ConversationState> {
    @Override
    public Map<String, Object> apply(ConversationState state) {
        System.out.println("ResponderNodeAction:");
        System.out.println("    Current state.toString(): " + state.toString());
        List<String> currentMessages = state.messages();
        String outputMessage = "";
        if (currentMessages.contains("Hi, this is greeter.")) {
            outputMessage = "This responder. I acknowledged greeting!";
        }
        else {
            outputMessage = "This responder. No greeting found.";
        }
        System.out.println("    Output message: " + outputMessage);
        System.out.println();

        return Map.of(ConversationState.MESSAGES_KEY, outputMessage);
    }
}
