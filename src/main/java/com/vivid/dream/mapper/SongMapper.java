package com.vivid.dream.mapper;

import com.vivid.dream.entity.SongDetail;
import com.vivid.dream.entity.SongLyrics;
import com.vivid.dream.entity.Song;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface SongMapper {
    Optional<Song> selectSong(Long melonId);

    List<Song> selectSongList(String date);

    int insertSong(Song song);

    Optional<SongDetail> selectSongDetail(Long melonId);

    int insertSongDetail(SongDetail song);

    Optional<SongLyrics> selectSongLyrics(Long melonId);

    int insertSongLyrics(SongLyrics SongLyrics);

    Optional<Integer> selectNowDataCount();
}