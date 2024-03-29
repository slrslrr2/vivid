package com.vivid.dream.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RabbitmqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(Integer msgType, String msg) {
        MessageProperties props = MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_BYTES).build();
        Message message = MessageBuilder.withBody(msg.getBytes(StandardCharsets.UTF_8)).andProperties(props).setHeader("ts",System.currentTimeMillis()).build();

        switch (msgType){
            case 1: {
                rabbitTemplate.send(MqContents.EXCHANGE_PRODUCT, MqContents.ROUTING_PRODUCT, message);
                break;
            }
            default: {
                log.warn("no type rabbitmq, type:{}, msg:{}", msgType, msg);
                break;
            }
        }
    }
}
