package com.archivision.community.util;

import org.springframework.stereotype.Component;

@Component
public class InputValidator {
    public boolean isNameValid(String name) {
        return !name.isBlank() && !name.isEmpty() && (name.length() <= 20 && name.length() > 2);
    }

    // TODO: 18.07.2023 use city repository to check the city is present in DB
    public boolean isCityValid(String city) {
        return !city.isBlank() && !city.isEmpty() && (city.length() <= 25 && city.length() >= 4);
    }

    public boolean isTopicValid(String topic) {
        return !topic.isBlank() && !topic.isEmpty() && (topic.length() <= 15 && topic.length() >= 3);
    }

    public boolean isDescriptionValid(String description) {
        return !description.isBlank() && !description.isEmpty() && (description.length() <= 255 && description.length() >= 25);
    }
}
