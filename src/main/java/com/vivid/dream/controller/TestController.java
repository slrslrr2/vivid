package com.vivid.dream.controller;

import com.vivid.dream.mq.RabbitmqProducer;
import com.vivid.dream.service.SongService;
import com.vivid.dream.vo.response.ResponseSong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
//    @Value("${spring.elasticsearch.host}")
//    String elasticsearchHost;

//    private final ElasticsearchClient elasticsearchClient;
    private final SongService songService;
    private final RabbitmqProducer rabbitmqProducer;

//    @GetMapping("/elastic/test")
//    public void elasticTest() throws IOException {
//        if (elasticsearchClient.exists(b -> b.index("products").id("foo")).value()) {
//            System.out.println("product exists");
//        } else {
//            System.out.println("product no exists");
//        }
//    }

    @GetMapping("/list")
    public List<ResponseSong> getSongList(String date){
        return songService.getSongList();
    }

//    @GetMapping("/properties")
//    public void propertiesList(){
//        log.info("변수가 찍히고 있다 TEST : =>  {} ", elasticsearchHost);
//    }

    @GetMapping("/rabbitMq")
    public void sendRabbitmq(){
        rabbitmqProducer.sendMessage(1, "{value: value, value2: value2}");
    }
}
