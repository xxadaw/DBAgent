package com.dbagent.task.controller;

import com.dbagent.common.core.controller.BaseController;
import com.dbagent.instance.constant.Result;
import com.dbagent.task.anomaly.domain.AnomalyResultRequest;
import com.dbagent.task.service.AnomalyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname AnomalyController
 * @Description TODO
 * @Date 2025/8/14 23:06
 * @Created by xxx
 */
@RestController
@Slf4j
@RequestMapping("/anomaly")
@RequiredArgsConstructor
public class AnomalyController extends BaseController {

    private final AnomalyService anomalyService;

    @RequestMapping("/list")
    public Result list(@RequestBody AnomalyResultRequest anomalyResultRequest) {
        log.info("list request: {}", anomalyResultRequest);
        return anomalyService.list(anomalyResultRequest);
    }
}
