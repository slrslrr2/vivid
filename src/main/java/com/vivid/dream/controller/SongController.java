package com.vivid.dream.controller;

import com.vivid.dream.model.SongVo;
import com.vivid.dream.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/song")
public class SongController {
    private final SongService songService;

    @GetMapping("/list")
    public List<SongVo> getSongList(String date){
        return songService.getSongList(date);
    }
}
