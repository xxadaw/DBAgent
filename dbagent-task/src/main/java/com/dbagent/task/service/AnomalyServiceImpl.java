package com.dbagent.task.service;

import com.dbagent.instance.constant.Result;
import com.dbagent.task.anomaly.domain.AnomalyResult;
import com.dbagent.task.anomaly.domain.AnomalyResultRequest;
import com.dbagent.task.mapper.AnomalyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname AnomalyServiceImpl
 * @Description TODO
 * @Date 2025/8/14 23:10
 * @Created by xxx
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnomalyServiceImpl implements AnomalyService{

    private final AnomalyMapper anomalyMapper;

    @Override
    public Result list(AnomalyResultRequest anomalyResultRequest) {
        if(anomalyResultRequest.getOffset() == null || anomalyResultRequest.getLimit() == null){
            anomalyResultRequest.setOffset(0);
            anomalyResultRequest.setLimit(20);
        }
        List<AnomalyResult> anomalyResults = anomalyMapper.getAnomalyResults(anomalyResultRequest);
        log.info("list response: {}", anomalyResults);
        return Result.success(anomalyResults);
    }
}
