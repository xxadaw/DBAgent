package com.dbagent.instance.service;

import com.dbagent.instance.service.impl.MysqlInstanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Classname MysqlInstanceServiceTest
 * @Description TODO
 * @Date 2025/8/10 22:34
 * @Created by xxx
 */
@SpringBootTest
class MysqlInstanceServiceTest {
    @Test
    void executeSql() {
        MysqlInstanceService service = new MysqlInstanceServiceImpl();
    }
}