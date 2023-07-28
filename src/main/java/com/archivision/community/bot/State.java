package com.archivision.community.bot;

import lombok.Getter;

@Getter
public enum State {
    START("start"),
    NAME("name"),
    CITY("city"),
    AGE("age"),
    GENDER("gender"),
    LOOKING("looking"),
    TOPIC("topic"),
    DESCRIPTION("description"),
    APPROVE("approve"),
    MATCH("match"),
    PHOTO("photo");

    private final String value;
    State(String value) {
        this.value = value;
    }
}
