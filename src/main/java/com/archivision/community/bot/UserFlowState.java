package com.archivision.community.bot;

import lombok.Getter;

@Getter
public enum UserFlowState {
    START("start"),
    TYPE("type"),
    NAME("name"),
    CITY("city"),
    AGE("age"),
    GENDER("gender"),
    LOOKING("looking"),
    TOPIC("topic"),
    DESCRIPTION("description"),
    APPROVE("approve"),
    MATCH("match"),
    PHOTO("photo"),
    SETTINGS("settings"),
    STATS("stats");

    private final String value;
    UserFlowState(String value) {
        this.value = value;
    }
}
