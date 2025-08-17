package com.dbagent.agent.mutiagent;

import com.dbagent.agent.workflow.State;
import com.dbagent.agent.workflow.StateSerializer;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.V;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.NodeAction;
import org.bsc.langgraph4j.utils.EdgeMappings;

import java.util.Map;

import static java.lang.String.format;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;


/**
 * @Classname SupervisorAgent
 * @Description TODO
 * @Date 2025/8/16 18:03
 * @Created by xxx
 */
@Slf4j
class SupervisorAgent implements NodeAction<State> {

    public final String[] members = {"researcher", "coder"};
    final Service service;

    public SupervisorAgent(ChatModel model) {

        service = AiServices.create(Service.class, model);
    }

    @Override
    public Map<String, Object> apply(State state) throws Exception {
        var message = state.lastMessage().orElseThrow();
        var text = switch (message.type()) {
            case USER -> ((UserMessage) message).singleText();
            case AI -> ((AiMessage) message).text();
            default -> throw new IllegalStateException("unexpected message type: " + message.type());
        };
        var m = String.join(",", members);

        var result = service.evaluate(m, text);

        return Map.of("next", result.next);
    }

    interface Service {
        @SystemMessage("""
                You are a supervisor tasked with managing a conversation between the following workers: {{members}}.
                Given the following user request, respond with the worker to act next.
                Each worker will perform a task and respond with their results and status.
                When finished, respond with FINISH.
                """)
        Router evaluate(@V("members") String members, @dev.langchain4j.service.UserMessage String userMessage);
    }

    static class Router {
        @Description("Worker to route to next. If no workers needed, route to FINISH.")
        String next;

        @Override
        public String toString() {
            return format("Router[next: %s]", next);
        }
    }
}

/**
 * 研究机构
 *
 * @author xxx
 * @date 2025/08/16 19:37
 */
@Slf4j
class ResearchAgent implements NodeAction<State> {
    final Service service;

    public ResearchAgent(ChatModel model) {
        service = AiServices.builder(Service.class)
                .chatModel(model)
                .tools(new Tools())
                .build();
    }

    @Override
    public Map<String, Object> apply(State state) throws Exception {
        var message = state.lastMessage().orElseThrow();
        var text = switch (message.type()) {
            case USER -> ((UserMessage) message).singleText();
            case AI -> ((AiMessage) message).text();
            default -> throw new IllegalStateException("unexpected message type: " + message.type());
        };
        var result = service.search(text);
        return Map.of("messages", AiMessage.from(result));

    }

    interface Service {
        String search(@dev.langchain4j.service.UserMessage String query);
    }

    static class Tools {

        @Tool("""
                Use this to perform a research over internet
                """)
        String internetSearch(@P("internet query") String query) {
            log.info("search query: '{}'", query);
            return """
                    the games will be in Italy at Cortina '2026
                    """;
        }
    }
}

/**
 * 编码器代理
 *
 * @author xxx
 * @date 2025/08/16 19:41
 */
@Slf4j
class CoderAgent implements NodeAction<State> {
    final Service service;

    public CoderAgent(ChatModel model) throws Exception {
        service = AiServices.builder(Service.class)
                .chatModel(model)
                .tools(new Tools())
                .build();
    }

    @Override
    public Map<String, Object> apply(State state) {
        var message = state.lastMessage().orElseThrow();
        var text = switch (message.type()) {
            case USER -> ((UserMessage) message).singleText();
            case AI -> ((AiMessage) message).text();
            default -> throw new IllegalStateException("unexpected message type: " + message.type());
        };
        var result = service.evaluate(text);
        return Map.of("messages", AiMessage.from(result));
    }

    interface Service {
        String evaluate(@dev.langchain4j.service.UserMessage String code);
    }

    static class Tools {

        @Tool("""
                Use this to execute java code and do math. If you want to see the output of a value,
                you should print it out with `System.out.println(...);`. This is visible to the user.""")
        String executeCode(@P("coder request") String request) {
            log.info("CoderTool request: '{}'", request);
            return """
                    2
                    """;
        }
    }
}

/**
 * 业务流程
 *
 * @author xxx
 * @date 2025/08/16 19:47
 */
@Slf4j
public class WorkFlow {

    private final ChatModel model;

    public WorkFlow() {
        this.model = QwenChatModel.builder()
                .modelName("qwen-max")
                .apiKey("sk-790278de5b85487299ea5bd91f1bc859")
                .build();
    }

    public void run() throws Exception {
        var supervisor = new SupervisorAgent(model);
        var coder = new CoderAgent(model);
        var researcher = new ResearchAgent(model);

        var workflow = new StateGraph<>(State.SCHEMA,  new StateSerializer() )
                .addNode("supervisor", node_async(supervisor))
                .addNode("coder", node_async(coder))
                .addNode("researcher", node_async(researcher))
                .addEdge(START, "supervisor")
                .addConditionalEdges("supervisor",
                        edge_async(state ->
                                state.next().orElseThrow()
                        ),
                        EdgeMappings.builder()
                                .to("coder")
                                .to("researcher")
                                .toEND("FINISH")
                                .build())
                .addEdge("coder", "supervisor")
                .addEdge("researcher", "supervisor");
        var graph = workflow.compile();
        for( var event : graph.stream( Map.of( "messages", UserMessage.from("what are the result of 1 + 1 ?"))) ) {
            log.info( "{}", event );
        }
    }

}