package com.vivid.dream.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter @ToString
@NoArgsConstructor
public class ResultList<T> implements Serializable {
    private T data;
    private Integer totalCount;
}
