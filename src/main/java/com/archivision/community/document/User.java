package com.archivision.community.document;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private Long telegramUserId;
    private String name;
    private String city;
    private String description;
    private Long age;

    @Field("state")
    private State state = State.START;

    @DBRef
    private List<Topic> topics = new ArrayList<>();

    @Getter
    public enum State {
        START("start"),
        NAME("name"),
        CITY("city"),
        AGE("age"),
        TOPIC("topic"),
        DESCRIPTION("description"),
        APPROVE("approve"),
        MATCH("match");

        private final String value;
        State(String value) {
            this.value = value;
        }
    }
}
