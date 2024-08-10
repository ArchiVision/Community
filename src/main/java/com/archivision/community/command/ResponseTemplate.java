package com.archivision.community.command;

public interface ResponseTemplate {
    String START = """
            Привіт. Це Спільнота.\s
            
            Введи якомога більше інформації про себе, щоб заматчити схожих до твоїх інтересів людей.
            """;

    String TYPE_INPUT = "Вкажіть ваш тип (Юніт - компанія, проєкт, спільнота, etc):";
    String NAME_INPUT = "Твоє ім'я / назва юніту";
    String CITY_INPUT = "Місто";
    String AGE_INPUT = "Вік";
    String TOPICS_INPUT = "Введи теми, якими ти цікавишся";
    String DESC_INPUT = "Розкажіть коротко про себе";
    String PHOTO = "Можеш додати своє фото";
    String APPROVE_INPUT = "Нічого не забув, все вірно?";
    String GENDER_INPUT = "Гендер";
    String LOOKING_FOR_INPUT = "Кого шукаєте?";
}
