package com.vivid.dream.service;

import com.vivid.dream.vo.response.ResponsePopularSearchWord;
import com.vivid.dream.vo.response.ResponseSongInfoSearch;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    String getLastIndexName(String name) throws IOException;

    List<ResponseSongInfoSearch> getSongInfoSearch(String keyword) throws IOException;

    List<ResponsePopularSearchWord> getPopularSearchWords(int beforeDays, int limit) throws Exception;
}
