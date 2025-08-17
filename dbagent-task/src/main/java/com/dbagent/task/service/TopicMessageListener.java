package com.dbagent.task.service;

import com.dbagent.task.domain.InstanceMetrics;
import com.dbagent.task.mapper.MetricsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname TopicMessageListener
 * @Description TODO
 * @Date 2025/8/12 23:14
 * @Created by xxx
 */
@Service
@Slf4j
public class TopicMessageListener {

    private final static Map<String, Object> typeCache = new ConcurrentHashMap<>();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MetricsMapper metricsMapper;

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getType(String type) {
        if (typeCache.containsKey(type)) {
            return (Class<T>) typeCache.get(type);
        }
        try {
            Class<T> clazz = (Class<T>) Class.forName(type);
            typeCache.put(type, clazz);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "topic_metric", groupId = "group1")
    public void onMetricMessage(@Payload String message, @Header("type") String type) throws Exception {
        InstanceMetrics msg = objectMapper.readValue(message, getType(type));
        metricsMapper.addSystemMetrics(msg.getInstanceId(), msg.getSysMetrics());
        log.info("received metric message: {}", message);
    }
}
