package com.archivision.community.util;

import org.springframework.stereotype.Component;

@Component
public class InputValidator {
    public boolean isNameValid(String name) {
        return !name.isBlank() && !name.isEmpty() && name.length() <= 20;
    }

    public boolean isCityValid(String city) {
        return !city.isBlank() && !city.isEmpty() && city.length() <= 20;
    }
}
