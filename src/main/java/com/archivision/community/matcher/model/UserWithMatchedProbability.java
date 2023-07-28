package com.archivision.community.matcher.model;


import com.archivision.community.entity.User;

public record UserWithMatchedProbability(User user, Double probability) {}
