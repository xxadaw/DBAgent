package com.dbagent.task.anomaly.domain;

import lombok.Data;

/**
 * @Classname AnomalyResultRequest
 * @Description TODO
 * @Date 2025/8/14 23:08
 * @Created by xxx
 */
@Data
public class AnomalyResultRequest {
    private String instanceId;
    private String type;
    private String detail;
    private Integer offset;
    private Integer limit;
}
