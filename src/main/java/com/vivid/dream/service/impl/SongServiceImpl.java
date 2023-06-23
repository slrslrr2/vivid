package com.vivid.dream.service.impl;

import com.vivid.dream.entity.Song;
import com.vivid.dream.mapper.SongMapper;
import com.vivid.dream.service.SongService;
import com.vivid.dream.vo.response.ResponseSong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongMapper songMapper;

    @Override
    public List<ResponseSong> getSongList(String date) {
        List<Song> songList = songMapper.selectSongList(date);
        List<ResponseSong> result = new ArrayList<>();
        for (Song songVo : songList) {
            ResponseSong song = ResponseSong.builder()
                    .id(songVo.getId())
                    .melonId(songVo.getMelonId())
                    .songName(songVo.getSongName())
                    .artist(songVo.getArtist())
                    .ranking(songVo.getRanking())
                    .createDate(songVo.getCreateDate()).build();
            result.add(song);
        }
        return result;
    }
}
