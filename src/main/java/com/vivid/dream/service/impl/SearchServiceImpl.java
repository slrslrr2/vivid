package com.vivid.dream.service.impl;

import com.vivid.dream.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final RestClient restClient;

    @Override
    public Response getRecentIndex(String name) throws IOException {
        Request request = new Request("GET", "_cat/indices/song_info-*?v&s=index:desc");
        Response response = restClient.performRequest(request);
        return response;
    }

    @Override
    public String getSongInfoSearch(String keyword) {
        return "";
    }

}