package com.vivid.dream.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_song_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long melonId;

    @Column(nullable = false)
    private String albumName;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String releaseDate;

    @Column(nullable = false)
    private Integer genre;

    @Column(nullable = false)
    private LocalDateTime createDate;
}