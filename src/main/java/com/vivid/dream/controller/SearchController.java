package com.vivid.dream.controller;

import com.vivid.dream.config.handler.ResultCode;
import com.vivid.dream.config.handler.exception.WebException;
import com.vivid.dream.vo.response.ResponsePopularSearchWord;
import com.vivid.dream.service.SearchService;
import com.vivid.dream.vo.response.ResponseSongInfoSearch;
import com.vivid.dream.vo.ResultList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(("/search"))
public class SearchController {
    final private SearchService searchService;

    @GetMapping("/song/info/{searchWord}")
    public ResponseEntity<?> getSongInfo(@PathVariable String searchWord){
        List<ResponseSongInfoSearch> songInfoSearch;
        try{
            songInfoSearch = searchService.getSongInfoSearch(searchWord);
        } catch (Exception e){
            throw new WebException(ResultCode.UNKNOWN_SEARCH, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResultList<>(songInfoSearch, songInfoSearch.size()), HttpStatus.OK);
    }
    
    @GetMapping("/popular/search/word/list")
    public ResponseEntity<?> getPopularSearchWords(@RequestParam(defaultValue = "30") int beforeDays,
                                                @RequestParam(defaultValue = "10") int limit){
        List<ResponsePopularSearchWord> popularSearchWords;
        try {
            popularSearchWords = searchService.getPopularSearchWords(beforeDays, limit);
        } catch (Exception e) {
            throw new WebException(ResultCode.GET_POPULAR_SEARCH_WORD_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(new ResultList<>(popularSearchWords, popularSearchWords.size()), HttpStatus.OK);
    }
}
