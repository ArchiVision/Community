package com.archivision.community.document;

import com.archivision.community.bot.State;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class User implements Serializable {
    @Id
    private String id;
    @Indexed(unique = true)
    private Long telegramUserId;
    private String name;
    private String city;
    private String description;
    private Long age;
    private String photoId;

    @Field("state")
    private State state = State.START;

    @DBRef
    private List<Topic> topics = new ArrayList<>();
}
