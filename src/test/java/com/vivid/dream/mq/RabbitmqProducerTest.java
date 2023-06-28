package com.vivid.dream.mq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqProducerTest {

    @Autowired
    private RabbitmqProducer rabbitmqProducer;

    @Test
    void send_message() {
        rabbitmqProducer.sendMessage(1, "{key: key, value:value}");
    }
}