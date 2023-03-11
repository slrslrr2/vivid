package com.vivid.dream.controller;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.vivid.dream.model.TestVo;
import com.vivid.dream.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final ElasticsearchClient elasticsearchClient;

    @RequestMapping("/db/test")
    public void dbTest(){
        List<TestVo> test = testService.test();
        for (TestVo testVo : test) {
            System.out.println(testVo.toString());
        }
    }

    @RequestMapping("/elastic/test")
    public void elasticTest() throws IOException {
        if (elasticsearchClient.exists(b -> b.index("products").id("foo")).value()) {
            System.out.println("product exists");
        } else {
            System.out.println("product no exists");
        }
    }
}
