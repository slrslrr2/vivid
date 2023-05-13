package com.vivid.dream.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PopularSearchWordVo {
    private String key;
    private Long docCount;
}
