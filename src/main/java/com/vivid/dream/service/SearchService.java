package com.vivid.dream.service;

import com.vivid.dream.vo.ResponsePopularSearchWord;
import com.vivid.dream.vo.ResponseSongInfoSearch;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    String getLastIndexName(String name) throws IOException;

    List<ResponseSongInfoSearch> getSongInfoSearch(String keyword) throws IOException;

    List<ResponsePopularSearchWord> getPopularSearchWords() throws Exception;
}
