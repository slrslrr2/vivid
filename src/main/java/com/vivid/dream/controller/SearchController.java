package com.vivid.dream.controller;

import com.vivid.dream.config.handler.ResultCode;
import com.vivid.dream.config.handler.exception.WebException;
import com.vivid.dream.model.PopularSearchWordVo;
import com.vivid.dream.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(("/search"))
public class SearchController {
    final private SearchService searchService;

    @GetMapping("/song/info")
    public ResponseEntity<?> getSongInfo(@RequestParam String searchWord){
        try{
            if (!"".equals(searchWord)) {
                System.out.println("keyword: "+ searchWord);
                return null;
            }
            // TODO: search queryDSL
            String indexName = searchService.getLastIndexName("song_info");
            System.out.println(indexName);
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
