package com.vivid.dream.config.kafka;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {
//    @Bean
//    public ProducerFactory<String, Long> producerFactory() {
//        Map<String, Object> config = new HashMap<>();
//
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
//
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    @Bean
//    public KafkaTemplate<String, Long> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
}
