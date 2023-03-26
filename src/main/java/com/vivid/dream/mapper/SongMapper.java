package com.vivid.dream.mapper;

import com.vivid.dream.model.SongDetailVo;
import com.vivid.dream.model.SongVo;

import java.util.Date;
import java.util.Optional;

//@Repository
//@Mapper
public interface SongMapper {
    Optional<SongVo> selectSong(Long melonId);

    int insertSong(SongVo song);

    int insertSongDetail(SongDetailVo song);

    Optional<Date> selectRecentDataBySong();
}