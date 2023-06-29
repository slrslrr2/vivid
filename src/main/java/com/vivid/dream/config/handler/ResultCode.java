package com.vivid.dream.config.handler;

public enum ResultCode {
    OK(0, "Request Successfully"),
    ERROR(1, "Error"),
    ERR_REQUEST_PARAMETER(2, "Request Parameter Error"),
    BAD_REQUEST(4, "Bad Request"),
    TODAY_SONG_TOP100_IS_EXIST(1001,  "The song top 100 is already existed."),
    GET_POPULAR_SEARCH_WORD_EXCEPTION(1002,  "An error occurred while bringing popular searches."),
    CREATE_SONG_BY_SCHEDULED_EXCEPTION(1003, "create Song Table Failed error"),
    CREATE_SONG_DETAIL_BY_SCHEDULED_EXCEPTION(1004, "create SongDetail Table Failed error"),
    CREATE_SONG_LYRICS_BY_SCHEDULED_EXCEPTION(1005, "create SongLyrics Table Failed error"),
    UNKNOWN_SEARCH(9999, "An unknown error occurred while searching. Please contact the administrator."),;
    private Integer code;
    private String desc;

    ResultCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
