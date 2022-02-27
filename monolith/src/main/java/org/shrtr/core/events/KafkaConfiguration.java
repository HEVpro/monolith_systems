package org.shrtr.core.events;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfiguration {

    @Value("${KAFKA-HOST}")
    private String host;
    @Value("${KAFKA-PORT}")
    private String port;

    @Bean
    public Admin kafkaAdmin() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, host + ":" + port);
        props.put(AdminClientConfig.RETRIES_CONFIG, "3");
        props.put(AdminClientConfig.RETRY_BACKOFF_MS_CONFIG, "2500");
        Admin admin = Admin.create(props);
        return admin;
    }

    @Bean
    public Producer<String, String> kafkaProducer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", host + ":" + port);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        return producer;
    }
}