package com.dbagent.task.anomaly;

import com.dbagent.instance.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname DBAnomalyDetector
 * @Description TODO
 * @Date 2025/8/13 22:41
 * @Created by xxx
 */
public interface DBAnomalyDetector {

    void detect();
    void detect(String instanceId);
}
