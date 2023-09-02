package com.vivid.dream.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {
//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
//        connectionFactory.setPublisherReturns(true);
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMandatory(true);
//
//        rabbitTemplate.setConfirmCallback((corrlationData, ack, cause) -> {
//            if (ack) {
//                log.info("mq: correlationData({}), ack({}), cause({})", corrlationData, ack, cause);
//                return;
//            } else {
//                log.error("ID: correlationData({}), ack({}), cause({})", corrlationData, ack, cause);
//            }
//        });
//        return rabbitTemplate;
//    }
}
