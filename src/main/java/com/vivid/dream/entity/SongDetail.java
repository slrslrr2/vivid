package com.vivid.dream.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
public class SongDetail {
    private Long id;
    private Long melonId;
    private String albumName;
    private String imgUrl;
    private String releaseDate;
    private Integer genre;
    private Date createDate;
}