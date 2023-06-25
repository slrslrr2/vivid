package com.vivid.dream.repository;

import com.vivid.dream.entity.SongLyrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongLyricsRepository extends JpaRepository<SongLyrics, Long> {
    List<SongLyrics> findAllByMelonId(Long melonId);
}
