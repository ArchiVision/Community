package com.archivision.community.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TopicDto {
    private String id;
    private String name;
}
