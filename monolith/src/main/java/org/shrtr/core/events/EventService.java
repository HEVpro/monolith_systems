package org.shrtr.core.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.shrtr.core.domain.entities.Link;
import org.shrtr.core.domain.entities.LinkMetric;
import org.shrtr.core.domain.entities.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final Producer<String, String> kafkaProducer;
    private final Admin admin;
    private final ObjectMapper objectMapper;

    private static final List<String> topics = List.of(User.class, Link.class, LinkMetric.class)
            .stream()
            .map(Class::getSimpleName).collect(Collectors.toList());

    @PostConstruct
    public void init() throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = admin.listTopics();
        Set<String> strings = listTopicsResult.names().get();
        topics.forEach(topic -> {
            if (!strings.contains(topic)) {
                int partitions = 1;
                short replicationFactor = 1;
                CreateTopicsResult result = admin.createTopics(Collections.singleton(
                        new NewTopic(topic, partitions, replicationFactor)));
                KafkaFuture<Void> future = result.values().get(topic);
                try {
                    future.get();
                    log.info("Topic {} created", topic);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void entityEvent(Object entity, String operation) {

        try {
            Map<String, String> body = new HashMap<>();
            body.put("entity", objectMapper.writeValueAsString(entity));
            body.put("operation", operation);
            body.put("created", String.valueOf(System.currentTimeMillis()));
            ProducerRecord<String, String> event = new ProducerRecord<>(
                    entity.getClass().getSimpleName(),
                    objectMapper.writeValueAsString(body)// parse json
            );
            kafkaProducer.send(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void entityCreated(Object entity) {
        entityEvent(entity, "create");
    }

    public void entityDeleted(Object entity) {
        entityEvent(entity, "delete");
    }

    public void entityUpdated(Object entity) {
        entityEvent(entity, "update");
    }
}