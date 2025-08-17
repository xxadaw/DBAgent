package com.dbagent.agent.workflow;

import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.List;
import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * @Classname IntentAnalyzerNodeAction
 * @Description TODO
 * @Date 2025/8/16 15:51
 * @Created by xxx
 */
class IntentAnalyzerNodeAction implements NodeAction<ConversationState> {
    @Override
    public Map<String, Object> apply(ConversationState state) throws Exception {
        System.out.println("IntentAnalyzerNodeAction:");
        System.out.println("    Current state.toString(): " + state.toString());

        List<String> messages = state.messages();
        String lastMessage = messages.isEmpty() ? "" : messages.get(messages.size() - 1);

        // Simple intent detection logic
        String intent;
        boolean needsTool = false;

        if (lastMessage.toLowerCase().contains("weather")) {
            intent = "weather_query";
            needsTool = true;
        } else if (lastMessage.toLowerCase().contains("calculate") ||
                lastMessage.toLowerCase().contains("math")) {
            intent = "calculation";
            needsTool = true;
        } else {
            intent = "general_chat";
        }

        System.out.println("    User intent: " + intent);
        System.out.println("    Needs tool:  " + needsTool);

        String outputMessage = "Intent analyzed: " + intent;
        System.out.println("    Output message: " + outputMessage);
        System.out.println();

        return Map.of(
                ConversationState.USER_INTENT_KEY, intent,
                ConversationState.NEEDS_TOOL_KEY, needsTool,
                ConversationState.MESSAGES_KEY, outputMessage
        );
    }
}

/**
 * 工具执行器节点操作
 *
 * @author xxx
 * @date 2025/08/16 15:55
 */
class ToolExecutorNodeAction implements NodeAction<ConversationState> {
    @Override
    public Map<String, Object> apply(ConversationState state) {
        System.out.println("ToolExecutorNodeAction:");
        System.out.println("    Current state.toString(): " + state.toString());

        String result;
        switch (state.userIntent()) {
            case "weather_query":
                // We call external weather service to query
                result = "Current weather: 68°F, sunny with light clouds";
                break;
            case "calculation":
                // We call math service to calculate the result
                result = "Calculation result: 625(the answer to everything!)";
                break;
            default:
                result = "No tool needed for this request";
        }

        System.out.println("    Tool output: " + result);
        System.out.println("    Output message: " + "Tool executed: " + result);
        System.out.println();

        return Map.of(
                ConversationState.TOOL_RESULT_KEY, result,
                ConversationState.MESSAGES_KEY, "Tool executed: " + result
        );
    }
}

/**
 * 响应生成器节点动作
 *
 * @author xxx
 * @date 2025/08/16 16:00
 */
class ResponseGeneratorNodeAction implements NodeAction<ConversationState> {
    @Override
    public Map<String, Object> apply(ConversationState state) {
        System.out.println("ResponseGeneratorNodeAction:");
        System.out.println("    Current state.toString(): " + state.toString());

        String response;
        if (state.needsTool()) {
            response = "This response generator. Based on the tool execution: " + state.toolResult() + ". Is there anything else I can help you with?";
        }
        else {
            response = "This response generator. I understand you want to chat. How can I assist you today?";
        }
        System.out.println("    Output message: " + response);
        System.out.println();

        return Map.of(ConversationState.MESSAGES_KEY, response);
    }
}

/**
 * 路由边缘动作
 *
 * @author xxx
 * @date 2025/08/16 16:01
 */
class RoutingEdgeAction implements EdgeAction<ConversationState> {
    @Override
    public String apply(ConversationState state) {
        System.out.println("RoutingEdgeAction:");
        System.out.println("    Current state.toString(): " + state.toString());

        if (state.needsTool()) {
            System.out.println("    Routing to tool_executor");
            System.out.println();
            return "tool_executor";
        }
        else {
            System.out.println("    Routing to response_generator");
            System.out.println();
            return "response_generator";
        }
    }
}

/**
 * 条件路由图示例
 *
 * @author xxx
 * @date 2025/08/16 16:02
 */
public class ConditionalRoutingGraphExample {
    public static void main(String[] args) throws GraphStateException {
        // Create nodes
        IntentAnalyzerNodeAction    intentAnalyzer    = new IntentAnalyzerNodeAction();
        ToolExecutorNodeAction      toolExecutor      = new ToolExecutorNodeAction();
        ResponseGeneratorNodeAction responseGenerator = new ResponseGeneratorNodeAction();
        RoutingEdgeAction           routingEdge       = new RoutingEdgeAction();

        /*
         * Build graph with conditional routing:
         * START -> intent_analyzer -> tool_executor -> response_generator -> END
         *                          |                |
         *                          +----------------+
         */
        StateGraph<ConversationState> stateGraph = new StateGraph<>(ConversationState.SCHEMA, ConversationState::new)
                .addNode("intent_analyzer",    node_async(intentAnalyzer))
                .addNode("tool_executor",      node_async(toolExecutor))
                .addNode("response_generator", node_async(responseGenerator))

                // Define flow with conditional routing
                .addEdge(START, "intent_analyzer")
                .addConditionalEdges("intent_analyzer", edge_async(routingEdge),
                        Map.of("tool_executor",      "tool_executor",
                                "response_generator", "response_generator"))
                .addEdge("tool_executor",      "response_generator")
                .addEdge("response_generator", END);

        // Compile graph
        CompiledGraph<ConversationState> compiledGraph = stateGraph.compile();

        // Test with different user messages
        String[] userMessages = {
                "How's the weather today in San Francisco?",
                "Can you calculate 25 * 25?",
                "Hi, there! I just want to chat.",
        };

        for (String userMessage : userMessages) {
            // Init State
            Map<String, Object> initialState = Map.of(ConversationState.MESSAGES_KEY, userMessage);
            // Run
            compiledGraph.invoke(initialState)
                    .ifPresent(finalState -> {
                                System.out.println("Final State: " + finalState);
                                System.out.println("Final Messages: " + finalState.messages());
                            }
                    );
        }
    }
}