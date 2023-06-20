package com.vivid.dream.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class SongLyricsVo {
    private Long id;
    private Long melonId;
    private String songName;
    private String artist;
    private String lyrics;
    private Date createDate;
}