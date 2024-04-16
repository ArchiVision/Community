package com.archivision.common.model.dto;


import com.archivision.common.model.bot.UserFlowState;
import com.archivision.common.model.entity.Gender;
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
    private List<TopicDto> topics = new ArrayList<>();
}