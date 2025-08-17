package com.dbagent.task.service;

import com.dbagent.instance.constant.Result;
import com.dbagent.task.anomaly.domain.AnomalyResultRequest;

/**
 * @Classname AnomalyService
 * @Description TODO
 * @Date 2025/8/14 23:10
 * @Created by xxx
 */
public interface AnomalyService {
    Result list(AnomalyResultRequest anomalyResultRequest);
}
