package com.archivision.community.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectionManager {
    private final MongoOperations mongoOperations;

    @PostConstruct
    public void init() {
        dropCollection("users");
        dropCollection("topics");
    }

    public void createCollection(String collectionName) {
        mongoOperations.createCollection(collectionName);
    }

    public void dropCollection(String collectionName) {
        mongoOperations.dropCollection(collectionName);
    }
}
