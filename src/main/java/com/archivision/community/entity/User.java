package com.archivision.community.entity;

import com.archivision.community.bot.State;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "topics")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramUserId;
    private String name;
    private String city;
    private Long age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Gender lookingFor = Gender.ANYONE;
    private String description;
    private String photoId;

    @Enumerated(EnumType.STRING)
    private State state = State.START;

    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(
            name = "user_topic",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    @ToString.Exclude
    private Set<Topic> topics = new HashSet<>();
}
