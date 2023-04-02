package com.vivid.dream.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.vivid.dream.mapper.SongMapper;
import com.vivid.dream.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@EnableAspectJAutoProxy(exposeProxy = true)
public class TestController {

    private final ElasticsearchClient elasticsearchClient;
    private final SongService songService;

    @RequestMapping("/elastic/test")
    public void elasticTest() throws IOException {
        if (elasticsearchClient.exists(b -> b.index("products").id("foo")).value()) {
            System.out.println("product exists");
        } else {
            System.out.println("product no exists");
        }
    }
}
