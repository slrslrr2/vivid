package com.vivid.dream.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "tb_song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long melonId;

    @Column(nullable = false)
    private String songName;

    @Column(nullable = false)
    private String artist;

    private Integer ranking;

    @Column(nullable = false)
    private LocalDateTime createDate;
}
