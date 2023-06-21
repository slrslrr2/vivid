package com.vivid.dream.service;

import com.vivid.dream.vo.response.ResponseSong;
import org.jsoup.nodes.Element;

import java.util.List;

public interface SongService {
    void createSongByMelonChart(Element tr);

    List<ResponseSong> getSongList(String date);
}
