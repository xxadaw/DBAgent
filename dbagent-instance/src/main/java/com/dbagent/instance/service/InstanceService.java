package com.dbagent.instance.service;

import com.dbagent.common.core.domain.R;
import com.dbagent.instance.constant.Result;
import com.dbagent.instance.domain.Instance;
import com.dbagent.instance.domain.RequestInstance;

import java.util.List;

/**
 * @Classname InstanceService
 * @Description TODO
 * @Date 2025/8/10 16:59
 * @Created by xxx
 */
public interface InstanceService {
    Result instanceList(RequestInstance requestInstance);

    Result addInstance(RequestInstance requestInstance);

    Result updateInstance(RequestInstance requestInstance);

    Result deleteInstance(RequestInstance requestInstance);

    Instance getInstanceByInstanceId(String instanceId);

    List<String> getInstanceIds();
}
