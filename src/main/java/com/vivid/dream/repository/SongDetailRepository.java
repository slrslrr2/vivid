package com.vivid.dream.repository;

import com.vivid.dream.entity.SongDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongDetailRepository extends JpaRepository<SongDetail, Long> {
    List<SongDetail> findAllByMelonId(Long melonId);

}
