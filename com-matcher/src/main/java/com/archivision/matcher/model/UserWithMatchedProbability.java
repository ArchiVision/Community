package com.archivision.matcher.model;


import com.archivision.common.model.entity.User;

public record UserWithMatchedProbability(User user, Double probability) {}
