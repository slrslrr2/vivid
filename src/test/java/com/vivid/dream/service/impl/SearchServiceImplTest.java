package com.vivid.dream.service.impl;

import com.vivid.dream.service.SearchService;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchServiceImplTest {
    @Autowired private RestClient restClient;
    @Autowired private SearchService searchService;

    @Test
    @DisplayName("Elasticsearch Index 존재 및 status 조회")
    void exist_elasticsearch_index(){
        try {
            String searchKeyword = "song_info";
            Request request = new Request("GET", "_cat/indices/"+ searchKeyword +"-*?s=index:desc&h=index,health");
            Response response = restClient.performRequest(request);

            InputStream inputStream = response.getEntity().getContent();
            List<String> indexes = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.toList());

            String[] nowIndex = indexes.get(0).split(" ");
            String findIndexName = nowIndex[0];
            assertAll(
                    () -> assertTrue(indexes.size() > 1, () -> searchKeyword + " 관련 INDEX가 존재하지않습니다"),
                    () -> assertNotNull(findIndexName, () -> searchKeyword + " 관련 INDEX가 존재하지않습니다"),
                    () -> assertThat(findIndexName, containsString(searchKeyword)),
                    () -> assertThat(nowIndex[1], anyOf(is("green"), is("yellow")))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void get_last_index_name() {
        try {
            String lastIndexName = searchService.getLastIndexName("song_info");
            assertNotNull(lastIndexName);
        } catch (Exception e){

        }
    }
}