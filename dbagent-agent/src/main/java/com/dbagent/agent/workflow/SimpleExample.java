package com.dbagent.agent.workflow;

import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;

import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * @Classname SimpleExample
 * @Description TODO
 * @Date 2025/8/16 15:44
 * @Created by xxx
 */

//START -> greeter -> processor -> responder -> END
public class SimpleExample {
    public static void main(String[] args) throws GraphStateException {
        GreeterNodeAction greeterNodeAction = new GreeterNodeAction();
        ProcessorNodeAction processorNodeAction = new ProcessorNodeAction();
        ResponderNodeAction responderNodeAction = new ResponderNodeAction();
        StateGraph<ConversationState> graph = new StateGraph<>(ConversationState.SCHEMA, ConversationState::new)
                .addNode("greeter", node_async(greeterNodeAction))
                .addNode("processor", node_async(processorNodeAction))
                .addNode("responder", node_async(responderNodeAction))
                .addEdge(START, "greeter")
                .addEdge("greeter", "processor")
                .addEdge("processor", "responder")
                .addEdge("responder", END);
        CompiledGraph<ConversationState> compiledGraph = graph.compile();

        Map<String, Object> initialState = Map.of(ConversationState.MESSAGES_KEY, "User Message: Hi, there!");

        compiledGraph.invoke(initialState)
                .ifPresent(finalState -> {
                            System.out.println("Final State: " + finalState);
                            System.out.println("Final Messages: " + finalState.messages());
                            System.out.println("Final Step Count: " + finalState.stepCount());
                        }
                );
    }
}
