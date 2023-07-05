package com.vivid.dream.repository;

import com.vivid.dream.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.startDate >= :startDate")
    List<Event> findAllByStartDateAndEndDate(LocalDateTime startDate);
}