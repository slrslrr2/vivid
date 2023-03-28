package com.vivid.dream.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    private final ElasticsearchClient elasticsearchClient;

    @RequestMapping("/elastic/test")
    public void elasticTest() throws IOException {
        if (elasticsearchClient.exists(b -> b.index("products").id("foo")).value()) {
            System.out.println("product exists");
        } else {
            System.out.println("product no exists");
        }
    }
}
