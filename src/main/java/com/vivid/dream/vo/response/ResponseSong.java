package com.vivid.dream.vo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class ResponseSong {
    private Long id;
    private Long melonId;
    private String songName;
    private String artist;
    private Integer ranking;
    private Date createDate;
}