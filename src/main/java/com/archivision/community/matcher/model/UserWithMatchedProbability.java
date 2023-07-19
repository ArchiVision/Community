package com.archivision.community.matcher.model;

import com.archivision.community.document.User;

public record UserWithMatchedProbability(User user, Double probability) {}
