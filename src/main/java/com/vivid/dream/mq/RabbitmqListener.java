package com.vivid.dream.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitmqListener {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MqContents.QUEUE_PRODUCT),
            exchange = @Exchange(value = MqContents.EXCHANGE_PRODUCT, type = ExchangeTypes.DIRECT),
            key = MqContents.ROUTING_PRODUCT
    ))
    public void createProduct(String data, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
        try{
            log.warn("실행1 deliveryTag: {}, channel:{} ", deliveryTag, channel);
            Thread.sleep(10 * 1000);
            channel.basicAck(deliveryTag, false);
            log.warn("실행2 deliveryTag: {}, channel:{} ", deliveryTag, channel);
        } catch (Exception e){
            try {
                log.warn("실행3 deliveryTag: {}, channel:{} ", deliveryTag, channel);
                channel.basicNack(deliveryTag, false, true);
                log.warn("실행4 deliveryTag: {}, channel:{} ", deliveryTag, channel);

            } catch (Exception e2){
                log.warn("실행5 RabbitmqListener Exception {}", e2);
            }
        } finally {

        }
    }
}
