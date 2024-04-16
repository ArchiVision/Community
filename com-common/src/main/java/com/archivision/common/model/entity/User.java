package com.archivision.common.model.entity;

import com.archivision.common.model.bot.UserFlowState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "topics")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private Long telegramUserId;
    private String name;
    private String username;
    private String city;
    private Long age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Gender lookingFor = Gender.ANYONE;
    private String description;
    private String photoId;
    @Enumerated(EnumType.STRING)
    private UserFlowState userFlowState = UserFlowState.START;
    @Enumerated(EnumType.STRING)
    private Subscription subscription = Subscription.NONE;

    @ManyToMany(cascade = {PERSIST, MERGE}, fetch = LAZY)
    @JoinTable(
            name = "user_topic",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    @ToString.Exclude
    private Set<Topic> topics = new HashSet<>();

    public enum Subscription {
        VIP,
        PREMIUM,
        NONE
    }
}
