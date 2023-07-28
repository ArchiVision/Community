package com.archivision.community.command;

public interface ResponseTemplate {
    String START = """
            Привіт. Це Спільнота.\s
            
            Введи якомога більше інформації про себе, щоб заматчити тобі схожих до твоїх інтересів людей.
            
            Деякі пункти зможеш пропустит - можливо ти тут від нудьги і/або хочеш просто з кимось познайомитись.
            """;

    String NAME_INPUT = "Твоє ім'я";
    String CITY_INPUT = "Місто";
    String AGE_INPUT = "Вік";
    String TOPICS_INPUT = "Введи теми, якими ти цікавишся";
    String DESC_INPUT = "Розкажіть коротко про себе";
    String PHOTO = "Можеш додати своє фото";
    String APPROVE_INPUT = "Нічого не забув, все вірно?";
    String GENDER_INPUT = "Гендер";
    String LOOKING_FOR_INPUT = "Кого шукаєте?";
    String YOUR_PROFILE = """
            %s, %s, %s
            
            Теми: %s
            
            Опис: %s
            """;
}
