package com.dbagent.instance.service.impl;

import com.dbagent.instance.constant.Result;
import com.dbagent.instance.domain.Instance;
import com.dbagent.instance.domain.RequestExecuteSql;
import com.dbagent.instance.domain.RequestInstance;
import com.dbagent.instance.mapper.InstanceMapper;
import com.dbagent.instance.service.InstanceService;
import com.dbagent.instance.service.MysqlInstanceService;
import com.dbagent.instance.utils.MySqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @Classname InstanceServiceImpl
 * @Description TODO
 * @Date 2025/8/10 16:59
 * @Created by xxx
 */
@Service
@Slf4j
public class InstanceServiceImpl implements InstanceService {
    @Autowired
    private InstanceMapper instanceMapper;


    @Override

    public Result instanceList(RequestInstance requestInstance) {
        List<Instance> instances = instanceMapper.getInstances(requestInstance);
        instances = instances.stream()
                .peek(instance ->
                        instance.setStatus(MySqlUtil.ping(instance.getHost(), instance.getPort().toString(), instance.getUsername(), MySqlUtil.aesDecrypt(instance.getPasswordEnc(), MySqlUtil.DEFAULT_KEY))))
                .filter(instance -> requestInstance.getStatus() == -1 || Objects.equals(requestInstance.getStatus(), instance.getStatus()))
                .toList();
        log.info("instanceList response: {}", instances);
        return Result.success(instances);
    }


    @Override
    public Result addInstance(RequestInstance requestInstance) {
        instanceMapper.addInstance(requestInstance);
        return Result.success();
    }

    @Override
    public Result updateInstance(RequestInstance requestInstance) {
        instanceMapper.updateInstance(requestInstance);
        return Result.success();
    }

    @Override
    public Result deleteInstance(RequestInstance requestInstance) {
        instanceMapper.deleteInstance(requestInstance);
        return Result.success();
    }

    @Override
    public Instance getInstanceByInstanceId(String instanceId) {
        return instanceMapper.getInstance(instanceId);
    }

    @Override
    public List<String> getInstanceIds() {
        return instanceMapper.getInstanceIds();
    }
}
