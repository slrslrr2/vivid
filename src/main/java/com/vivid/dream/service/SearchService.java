package com.vivid.dream.service;

import org.elasticsearch.client.Response;

import java.io.IOException;

public interface SearchService {
    String getLastIndexName(String name) throws IOException;

    String getSongInfoSearch(String keyword);
}
