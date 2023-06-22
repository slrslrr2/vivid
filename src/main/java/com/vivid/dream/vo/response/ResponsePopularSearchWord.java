package com.vivid.dream.vo.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponsePopularSearchWord {
    private String key;
    private Long docCount;
}
