package com.archivision.community.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "topics")
@Data
public class Topic {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
}
