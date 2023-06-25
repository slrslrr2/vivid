package com.vivid.dream.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_song_lyrics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongLyrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long melonId;

    @Column(nullable = false)
    private String songName;

    @Column(nullable = false)
    private String artist;

    @Lob
    @Column(nullable = false)
    private String lyrics;

    @Column(nullable = false)
    private LocalDateTime createDate;
}