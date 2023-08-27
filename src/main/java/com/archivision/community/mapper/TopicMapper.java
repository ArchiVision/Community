package com.archivision.community.mapper;

import com.archivision.community.dto.TopicDto;
import com.archivision.community.entity.Topic;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {
    public TopicDto toDto(Topic topic) {
        TopicDto topicDto = new TopicDto();
        topicDto.setId(topic.getId().toString());
        topicDto.setName(topic.getName());
        return topicDto;
    }

    public Topic toEntity(TopicDto topicDto) {
        Topic topic = new Topic();
        topic.setId(topicDto.getId() != null ? Long.valueOf(topicDto.getId()) : null);
        topic.setName(topicDto.getName());
        return topic;
    }
}

