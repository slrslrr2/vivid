package com.vivid.dream.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
public class SongDetailVo {
    private Long id;
    private Long melonId;
    private String albumName;
    private String imageUrl;
    private String imageThumbUrl;
    private String releaseDate;
    private Integer genre;
    private Date createDate;
}