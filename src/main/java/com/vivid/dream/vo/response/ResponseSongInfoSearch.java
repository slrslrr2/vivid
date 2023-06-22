package com.vivid.dream.vo.response;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSongInfoSearch implements Serializable {
    private String songName;
    private String artist;

    private String artistByHighlight;
    private String songNameByHighlight;
    private String lyricsByHighlight;
}
