package com.vivid.dream.mapper;

import com.vivid.dream.entity.SongDetailVo;
import com.vivid.dream.entity.SongLyricsVo;
import com.vivid.dream.entity.SongVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface SongMapper {
    Optional<SongVo> selectSong(Long melonId);

    List<SongVo> selectSongList(String date);

    int insertSong(SongVo song);

    Optional<SongDetailVo> selectSongDetail(Long melonId);

    int insertSongDetail(SongDetailVo song);

    Optional<SongLyricsVo> selectSongLyrics(Long melonId);

    int insertSongLyrics(SongLyricsVo SongLyrics);

    Optional<Integer> selectNowDataCount();
}