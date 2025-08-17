package com.dbagent.task.service;

import com.dbagent.task.domain.InstanceMetrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Classname MessagingService
 * @Description TODO
 * @Date 2025/8/12 23:10
 * @Created by xxx
 */
@Service
@Slf4j
public class MessagingService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMetricsMessage(InstanceMetrics msg) throws IOException {
        send("topic_metric", msg);
    }


    private void send(String topic, Object msg) throws IOException {
        ProducerRecord<String, String> pr = new ProducerRecord<>(topic, objectMapper.writeValueAsString(msg));
        pr.headers().add("type", msg.getClass().getName().getBytes(StandardCharsets.UTF_8));
        log.info("send message: {}", msg);
        kafkaTemplate.send(pr);
    }
}
