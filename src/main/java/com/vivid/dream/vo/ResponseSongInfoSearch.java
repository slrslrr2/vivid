package com.vivid.dream.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ResponseSongInfoSearch {
    private String songName;
    private String artist;

    private String artistByHighlight;
    private String songNameByHighlight;
    private String lyricsByHighlight;
}
