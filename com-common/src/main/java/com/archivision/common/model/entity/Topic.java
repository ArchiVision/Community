package com.archivision.common.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "users")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "topics")
    @ToString.Exclude
    private Set<User> users = new HashSet<>();
}
