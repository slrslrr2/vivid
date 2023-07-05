package com.vivid.dream.enums;


import lombok.Getter;

import java.util.Arrays;

public enum EventStatus {
    PREPARATION(0, "Preparation"),
    ONGOING(1, "Ongoing"),
    COMPLETED(2, "Completed");

    @Getter
    private final Integer value;

    @Getter
    private final String description;

    EventStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static EventStatus findByDescription(String description) {
        return Arrays.stream(EventStatus.values())
                .filter(e -> description.equals(e.getDescription()))
                .findFirst()
                .orElse(EventStatus.COMPLETED);
    }
}