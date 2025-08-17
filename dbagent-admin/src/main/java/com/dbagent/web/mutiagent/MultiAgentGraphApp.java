package com.dbagent.web.mutiagent;

/**
 * @Classname MultiAgentGraphApp
 * @Description TODO
 * @Date 2025/8/16 00:12
 * @Created by xxx
 */

import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

/**
 * 多代理图形应用程序
 *
 * @author xxx
 * @date 2025/08/16 00:14
 */
public class MultiAgentGraphApp {

    public void run(){
        // 初始化节点
        RouterNode router = new RouterNode();
        WeatherNode weather = new WeatherNode();
        FinanceNode finance = new FinanceNode();
        GeneralNode general = new GeneralNode();

        // 定义状态图
        var stateGraph = new StateGraph<>(SimpleAgentState.SCHEMA, state -> new SimpleAgentState(new HashMap<>()))
                .addNode("router", node_async(router))
                .addNode("weather_agent", node_async(weather))
                .addNode("finance_agent", node_async(finance))
                .addNode("general_agent", node_async(general))
                .addEdge(START, "router")
                // 多分支，根据 query 动态选择下一节点
                .addConditionalEdges("router", state -> {
                    String query = (String) state.get("query");
                    if (query.toLowerCase().contains("weather")) return List.of("weather_agent");
                    if (query.toLowerCase().contains("finance")) return List.of("finance_agent");
                    return List.of("general_agent");
                })
                .addEdge("weather_agent", END)
                .addEdge("finance_agent", END)
                .addEdge("general_agent", END);

        // 编译图
        var compiledGraph = stateGraph.compile();

        // 交互式执行
        Scanner scanner = new Scanner(System.in);
        Map<String, Object> initState = new HashMap<>();
        System.out.print("Enter your query: ");
        String query = scanner.nextLine();
        initState.put(SimpleAgentState.MESSAGES_KEY, query);
        initState.put("query", query);

        var iterator = compiledGraph.stream(initState).iterator();
        Map<String,Object> currentState = initState;

        while (iterator.hasNext()) {
            var item = iterator.next();
            System.out.println("Current messages: " + item.data().get(SimpleAgentState.MESSAGES_KEY));

            // 用户控制是否继续
            System.out.print("Continue to next node? (y/n) ");
            String input = scanner.nextLine();
            if (!"y".equalsIgnoreCase(input)) {
                System.out.println("Execution paused by user.");
                break;
            }

            // 用户可动态注入提示词
            System.out.print("Add extra prompt (or leave empty): ");
            String extra = scanner.nextLine();
            if (!extra.isBlank()) {
                currentState = new HashMap<>(item.data());
                currentState.put("query", extra);
                currentState.put(SimpleAgentState.MESSAGES_KEY, extra);
            } else {
                currentState = item.data();
            }
        }

        System.out.println("Final state messages: " + currentState.get(SimpleAgentState.MESSAGES_KEY));
    }
}
