package com.vivid.dream.repository;

import com.vivid.dream.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    Long countByCreateDateAfter(LocalDateTime date);
    List<Song> findAllByCreateDate(LocalDateTime date);
}
