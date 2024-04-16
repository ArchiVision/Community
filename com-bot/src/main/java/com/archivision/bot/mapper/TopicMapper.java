package com.archivision.bot.mapper;

import com.archivision.common.model.entity.Topic;
import org.springframework.stereotype.Component;
import com.archivision.common.model.dto.TopicDto;

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

