package com.archivision.community.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "likes")
@Data
public class Like {
    @Id
    private String id;
    private LocalDateTime likeTime;
    private Long likeUserId;
    private Boolean notified;
}
