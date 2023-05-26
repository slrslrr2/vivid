package com.vivid.dream.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class SearchServiceImplTest {

    @InjectMocks
    private RestClient restClient;

    @InjectMocks
    public ElasticsearchClient elasticsearchClient;

    @Test
    void getRecentIndex() {
        try {
            Request request = new Request("GET", "_cat/indices/song_info-*?v&s=index:desc");
            Response response = restClient.performRequest(request);
            System.out.println(response);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}