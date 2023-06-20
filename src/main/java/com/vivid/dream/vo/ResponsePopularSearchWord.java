package com.vivid.dream.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponsePopularSearchWord {
    private String key;
    private Long docCount;
}
