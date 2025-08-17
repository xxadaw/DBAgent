package com.dbagent.task.controller;

import com.dbagent.common.core.controller.BaseController;
import com.dbagent.instance.constant.Result;
import com.dbagent.task.domain.InstanceMetricsRequest;
import com.dbagent.task.service.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname MetricsController
 * @Description TODO
 * @Date 2025/8/14 21:33
 * @Created by xxx
 */
@RestController
@RequestMapping("/metrics")
@Slf4j
public class MetricsController extends BaseController {

    @Autowired
    private MetricsService metricsService;

    @RequestMapping("/list")
    public Result list(@RequestBody InstanceMetricsRequest instanceMetricsRequest) {
        log.info("list request: {}", instanceMetricsRequest);
        return metricsService.list(instanceMetricsRequest);
    }
}