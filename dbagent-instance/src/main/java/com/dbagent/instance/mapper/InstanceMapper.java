package com.dbagent.instance.mapper;


import com.dbagent.instance.domain.Instance;
import com.dbagent.instance.domain.RequestInstance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Classname InstanceMapper.xml
 * @Description TODO
 * @Date 2025/8/10 17:03
 * @Created by xxx
 */
@Mapper
public interface InstanceMapper {

    List<Instance> getInstances(RequestInstance requestInstance);

    void addInstance(RequestInstance requestInstance);

    void updateInstance(RequestInstance requestInstance);

    void deleteInstance(RequestInstance requestInstance);

    Instance getInstance(String instanceId);

    List<String> getInstanceIds();
}