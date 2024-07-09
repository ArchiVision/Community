package com.archivision.community.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserType {
    PERSON("Особа", "Людей"),
    UNIT("Юніт", "Юніти"),
    ANYONE("Все одно", "Все одно");

    private final String typeChoose;
    private final String lookingChoose;

    public static UserType fromString(String value) {
        for (UserType type : UserType.values()) {
            if (isEquals(type.typeChoose, value) || isEquals(type.lookingChoose, value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid User Type value: " + value);
    }

    private static boolean isEquals(String field, String value) {
        return field.equalsIgnoreCase(value);
    }
}
