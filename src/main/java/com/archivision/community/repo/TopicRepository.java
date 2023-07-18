package com.archivision.community.repo;

import com.archivision.community.document.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {}
