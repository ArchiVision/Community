package com.archivision.community.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
    MAN("Хлопець", "Хлопців"),
    WOMAN("Дівчина", "Дівчат"),
    OTHER("Інше", "Інше"),
    ANYONE("Все одно", "Все одно");

    private final String genderChoose;
    private final String lookingChoose;

    public static Gender fromString(String value) {
        for (Gender gender : Gender.values()) {
            if (isEquals(gender.genderChoose, value) || isEquals(gender.lookingChoose, value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid Gender value: " + value);
    }

    private static boolean isEquals(String field, String value) {
        return field.equalsIgnoreCase(value);
    }
}
