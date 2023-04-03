package com.vivid.dream.service;

import com.vivid.dream.model.SongVo;
import org.jsoup.nodes.Element;

import java.util.List;

public interface SongService {
    void createSongByMelonChart(Element tr);

    List<SongVo> getSongList(String date);
}
