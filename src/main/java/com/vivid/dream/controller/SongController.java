package com.vivid.dream.controller;

import com.vivid.dream.entity.SongVo;
import com.vivid.dream.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/song")
@Slf4j
public class SongController {
    private final SongService songService;

    @GetMapping("/list")
    public List<SongVo> getSongList(@RequestParam String date){
        return songService.getSongList(date);
    }
}
