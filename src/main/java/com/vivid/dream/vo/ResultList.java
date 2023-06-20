package com.vivid.dream.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter @ToString
public class ResultList<T> {
    private T data;
    private Integer totalCount;
}
