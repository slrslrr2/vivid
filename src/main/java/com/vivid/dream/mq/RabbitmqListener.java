package com.vivid.dream.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitmqListener {
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = MqContents.QUEUE_PRODUCT),
//            exchange = @Exchange(value = MqContents.EXCHANGE_PRODUCT, type = ExchangeTypes.DIRECT),
//            key = MqContents.ROUTING_PRODUCT
//    ))
//    public void createProduct(String data, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
//        try{
//            Thread.sleep(10 * 1000);
//            channel.basicAck(deliveryTag, false);
//        } catch (Exception e){
//            log.warn("RabbitmqListener Exception1 {}", e);
//            try {
//                channel.basicNack(deliveryTag, false, true);
//            } catch (Exception e2){
//                log.warn("RabbitmqListener Exception2 {}", e2);
//            }
//        } finally {
//
//        }
//    }
}
