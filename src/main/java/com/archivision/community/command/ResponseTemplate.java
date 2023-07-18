package com.archivision.community.command;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface ResponseTemplate {
    String START = """
            Привіт. Це Спільнота.\s
            
            Введи якомога більше інформації про себе, щоб заматчити тобі схожих
            до твоїх інтересів людей.
            
            Деякі пункти зможеш пропустит - можливо ти тут від нудьги і/або
            хочеш просто з кимось познайомитись.
            """;

    String NAME_INPUT = "Твоє ім'я:";
    String CITY_INPUT = "Місто:";
    String TOPICS_INPUT = "Введи теми, якими ти цікавишся:";
    String DESC_INPUT = "Місто:";
    String APPROVE_INPUT = "Нічого не забув, все вірно?";
}
