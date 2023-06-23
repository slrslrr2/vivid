package com.vivid.dream.service;

import com.vivid.dream.vo.response.ResponseSong;

import java.util.List;

public interface SongService {
    List<ResponseSong> getSongList(String date);
}
