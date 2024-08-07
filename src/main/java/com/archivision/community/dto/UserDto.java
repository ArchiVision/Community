package com.archivision.community.dto;

import com.archivision.community.bot.UserFlowState;
import com.archivision.community.entity.Gender;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private String id;
    private Long telegramUserId;
    private String username;
    private String name;
    private String city;
    private String description;
    private Gender gender;
    private Gender lookingFor;
    private String photoId;
    private Long age;
    private UserFlowState userFlowState;

    private Long numberOfViews = 0L;
    private List<TopicDto> topics = new ArrayList<>();
}