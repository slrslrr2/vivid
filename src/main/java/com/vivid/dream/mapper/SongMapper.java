package com.vivid.dream.mapper;

import com.vivid.dream.model.SongDetailVo;
import com.vivid.dream.model.SongLyricsVo;
import com.vivid.dream.model.SongVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface SongMapper {
    Optional<SongVo> selectSong(Long melonId);

    List<SongVo> selectSongList();

    int insertSong(SongVo song);

    Optional<SongDetailVo> selectSongDetail(Long melonId);

    int insertSongDetail(SongDetailVo song);

    Optional<SongLyricsVo> selectSongLyrics(Long melonId);

    int insertSongLyrics(SongLyricsVo SongLyrics);

    Optional<Integer> selectNowDataCount();
}