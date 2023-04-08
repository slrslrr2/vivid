package com.vivid.dream.model;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
public class SongVo {
    private Long id;
    private Long melonId;
    private String songName;
    private String artist;
    private Integer rank;
    private Date createDate;
}