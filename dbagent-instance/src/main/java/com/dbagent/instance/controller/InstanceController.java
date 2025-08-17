package com.dbagent.instance.controller;

import com.dbagent.common.core.controller.BaseController;
import com.dbagent.common.utils.uuid.UUID;
import com.dbagent.instance.constant.Result;
import com.dbagent.instance.domain.RequestExecuteSql;
import com.dbagent.instance.domain.RequestInstance;
import com.dbagent.instance.service.InstanceService;
import com.dbagent.instance.service.MysqlInstanceService;
import com.dbagent.instance.utils.MySqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * @Classname InstanceController
 * @Description TODO
 * @Date 2025/8/10 16:51
 * @Created by xxx
 */
@RestController
@RequestMapping("/instance")
@Slf4j
public class InstanceController extends BaseController {

    @Autowired
    private InstanceService instanceService;
    @Autowired
    private MysqlInstanceService mysqlInstanceService;

    @PostMapping("/list")
    public Result instanceList(@RequestBody RequestInstance requestInstance) {
        Long userId = 1L;
        requestInstance.setUserId(userId);
        if (requestInstance.getOffset() == null || requestInstance.getLimit() == null
                || (requestInstance.getOffset() <= 0 && requestInstance.getLimit() <= 0)) {
            requestInstance.setOffset(0);
            requestInstance.setLimit(20);
        }
        if (requestInstance.getStatus() == null) {
            requestInstance.setStatus(-1);
        }
        log.info("instanceList request: {}", requestInstance);
        return instanceService.instanceList(requestInstance);
    }

    @PostMapping("/add")
    public Result addInstance(@RequestBody RequestInstance requestInstance) {
        if (MySqlUtil.ping(requestInstance.getHost(), String.valueOf(requestInstance.getPort()), requestInstance.getUsername(), requestInstance.getPasswordEnc()) == 0) {
            return Result.error("数据库连接失败");
        }
        Long userId = 1L;
        requestInstance.setUserId(userId);
        String instanceId = UUID.randomUUID().toString();
        requestInstance.setInstanceId(instanceId);
        MySqlUtil.InstanceInfoSupplement(requestInstance);
        requestInstance.setPasswordEnc(MySqlUtil.aesEncrypt(requestInstance.getPasswordEnc(), MySqlUtil.DEFAULT_KEY));
        log.info("addInstance request: {}", requestInstance);
        return instanceService.addInstance(requestInstance);
    }

    @PostMapping("/update")
    public Result updateInstance(@RequestBody RequestInstance requestInstance) {
        if (MySqlUtil.ping(requestInstance.getHost(), String.valueOf(requestInstance.getPort()), requestInstance.getUsername(), requestInstance.getPasswordEnc()) == 0) {
            return Result.error("数据库连接失败");
        }
        Long userId = 1L;
        requestInstance.setUserId(userId);
        requestInstance.setPasswordEnc(MySqlUtil.aesEncrypt(requestInstance.getPasswordEnc(), MySqlUtil.DEFAULT_KEY));
        String instanceId = UUID.randomUUID().toString();
        requestInstance.setInstanceId(instanceId);
        log.info("updateInstance request: {}", requestInstance);
        return instanceService.updateInstance(requestInstance);
    }

    @PostMapping("/delete")
    public Result deleteInstance(@RequestBody RequestInstance requestInstance) {
        Long userId = 1L;
        requestInstance.setUserId(userId);
        log.info("deleteInstance request: {}", requestInstance);
        return instanceService.deleteInstance(requestInstance);
    }

    @PostMapping("/executeSql")
    public Result executeSql(@RequestBody RequestExecuteSql requestExecuteSql) throws SQLException {
        log.info("executeSql request: {}", requestExecuteSql);
        Object result = mysqlInstanceService.executeSql(requestExecuteSql);
        return Result.success(result);
    }
}
