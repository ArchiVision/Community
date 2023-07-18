package com.archivision.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private String id;
    private Long telegramUserId;
    private String name;
    private String city;
    private String description;
    private Long age;
    private String state;
    private List<TopicDto> topics = new ArrayList<>();
}