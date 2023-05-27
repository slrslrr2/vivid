package com.vivid.dream.controller;

import com.vivid.dream.config.handler.ResultCode;
import com.vivid.dream.config.handler.exception.WebException;
import com.vivid.dream.model.PopularSearchWordVo;
import com.vivid.dream.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
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
        try{
            // TODO: search queryDSL

            searchService.getSongInfoSearch(searchWord);
        }catch (Exception e){
            throw new WebException(ResultCode.UNKNOWN_SEARCH, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @GetMapping("/popular/search/word/list")
    public ResponseEntity getPopularSearchWords(HttpServletRequest request){
        List<PopularSearchWordVo> popularSearchWords;
        try {
            popularSearchWords = searchService.getPopularSearchWords();
        } catch (Exception e) {
            throw new WebException(ResultCode.GET_POPULAR_SEARCH_WORD_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(popularSearchWords, HttpStatus.OK);
    }
}
