package com.dbagent.agent.controller;

import com.dbagent.agent.ai.DBHelperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * @Classname DBHelperController
 * @Description 数据库助手控制器
 * @Date 2025/8/9 12:26
 * @Created by xxx
 */
@RestController
@RequestMapping("/agent")
@Slf4j
@RequiredArgsConstructor
public class DBHelperController {

    private final DBHelperService dbHelperService;

    @RequestMapping("/optimizeSql")
    public String optimizeSql(String memoryId, String input) {
        DBHelperService.Report report = dbHelperService.optimizeSql(memoryId, input);
        return report.suggestions().toString();
    }

    @RequestMapping("/streamChat")
    public Flux<ServerSentEvent<String>> streamChat(String memoryId, String input) {
        if (memoryId.isEmpty()) {
            memoryId = UUID.randomUUID().toString();
        }
        log.info("memoryId:{},input:{}", memoryId, input);
        return dbHelperService.streamChat(memoryId, input)
                .map(chunk -> ServerSentEvent
                        .<String>builder()
                        .data(chunk)
                        .build());
    }
}