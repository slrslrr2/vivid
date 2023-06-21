package com.vivid.dream.entity;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
public class Song {
    private Long id;
    private Long melonId;
    private String songName;
    private String artist;
    private Integer ranking;
    private Date createDate;
}
