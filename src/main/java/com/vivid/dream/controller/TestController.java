package com.vivid.dream.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.vivid.dream.config.handler.ResultCode;
import com.vivid.dream.config.handler.exception.WebException;
import com.vivid.dream.model.SongVo;
import com.vivid.dream.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
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

    @GetMapping("/list")
    public List<SongVo> getSongList(String date){
        if("2022-04-03".equals(date)){
            throw new WebException(ResultCode.ERR_REQUEST_PARAMETER);
        }
        return songService.getSongList(date);
    }
}
