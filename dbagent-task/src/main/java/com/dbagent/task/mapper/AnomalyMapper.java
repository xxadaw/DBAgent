package com.dbagent.task.mapper;

import com.dbagent.task.anomaly.domain.AnomalyResult;
import com.dbagent.task.anomaly.domain.AnomalyResultRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Classname AnomalyMapper
 * @Description TODO
 * @Date 2025/8/13 23:23
 * @Created by xxx
 */
@Mapper
public interface AnomalyMapper {
    void addAnomaly(AnomalyResult anomalyResult);
    void addAnomalys(List<AnomalyResult> anomalyResults);
    void deleteAnomalyByInstanceIdAndType(String instanceId,String type);

    List<AnomalyResult> getAnomalyResults(AnomalyResultRequest anomalyResultRequest);
}
