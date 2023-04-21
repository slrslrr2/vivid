package com.vivid.dream.controller;

import com.vivid.dream.config.handler.ResultCode;
import com.vivid.dream.config.handler.exception.WebException;
import com.vivid.dream.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(("/search"))
public class SearchController {
    final private SearchService searchService;

    @GetMapping("/song/info")
    public ResponseEntity<?> getSongInfo(HttpServletRequest request){
        try{
            String indexName = searchService.getLastIndexName("song_info");
            System.out.println(indexName);
        }catch (Exception e){
            throw new WebException(ResultCode.UNKNOWN_SEARCH, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
