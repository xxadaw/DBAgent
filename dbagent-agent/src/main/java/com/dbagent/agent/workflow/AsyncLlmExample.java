package com.dbagent.agent.workflow;

import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.NodeAction;

import java.util.Map;
import java.util.Optional;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * @Classname AsyncLlmExample
 * @Description TODO
 * @Date 2025/8/16 16:12
 * @Created by xxx
 */
public class AsyncLlmExample {
    public static void main(String[] args) throws GraphStateException {
        // Async node that simulates LLM call
        NodeAction<ConversationState> llmNode = (state) -> {
            System.out.println("Simulating LLM call...");
            // Simulate processing time
            try {
                Thread.sleep(3000); // Simulate LLM response time
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return Map.of(
                    ConversationState.MESSAGES_KEY, "AI Response: I've analyzed your request asynchronously!",
                    ConversationState.STEP_COUNT_KEY, state.stepCount() + 1
            );
        };

        // Build async graph: START -> llm_node -> END
        StateGraph<ConversationState> stateGraph = new StateGraph<>(ConversationState.SCHEMA, ConversationState::new)
                .addNode("llm_node", node_async(llmNode))
                .addEdge(START, "llm_node")
                .addEdge("llm_node", END);
        // Compile graph
        CompiledGraph<ConversationState> compiledGraph = stateGraph.compile();
        // Init state
        Map<String, Object> initialState = Map.of(ConversationState.MESSAGES_KEY, "Process this asynchronously");
        // Run
        Optional<ConversationState> finalState = compiledGraph.invoke(initialState);

        System.out.println("Final Messages: " + finalState.get().messages());
        System.out.println("Final Step Count: " + finalState.get().stepCount());
    }
}
