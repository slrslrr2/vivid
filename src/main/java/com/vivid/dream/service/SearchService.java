package com.vivid.dream.service;

import com.vivid.dream.model.PopularSearchWordVo;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    String getLastIndexName(String name) throws IOException;

    String getSongInfoSearch(String keyword) throws IOException;

    List<PopularSearchWordVo> getPopularSearchWords() throws Exception;
}
