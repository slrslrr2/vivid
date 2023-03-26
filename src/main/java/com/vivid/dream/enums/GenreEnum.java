package com.vivid.dream.enums;

import lombok.Getter;

import java.util.Arrays;

public enum GenreEnum {
    ROCK_METAL(0, "록/메탈"),
    INDIE_MUSIC(1, "인디음악, 포크/블루스"),
    FOLK_BLUES(2, "포크/블루스"),
    JAZZ(3, "재즈"),
    BALLADE(4, "발라드"),
    J_POP(5, "J-POP"),
    ELECTRONICA(6, "일렉트로니카"),
    TROT(7, "성인가요/트로트"),
    BALLAD(8, "발라드, 인디음악"),
    HIP_HOP(9, "랩/힙합"),
    POP(10, "POP"),
    DANCE(11, "댄스"),
    BALLAD_DARAMA(12, "발라드, 국내드라마"),
    SOUL(13, "R&B/Soul"),
    ETC(14, "ETC");

    @Getter
    private Integer value;

    @Getter
    private String description;

    GenreEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static GenreEnum findGenreEnumByDescription(String description){
        return Arrays.stream(GenreEnum.values())
                .filter(e -> description.equals(e.getDescription()))
                .findFirst()
                .orElse(ETC);
    }

}
