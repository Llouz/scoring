package com.scoring.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    public User(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    @Id
    @GeneratedValue
    private UUID uuid;

    @SequenceGenerator(
            name = "user_id_seq_gen",
            sequenceName = "user_id_seq",
            allocationSize = 1
    )
    @Column(
            name = "id",
            nullable = false,
            unique = true,
            insertable = false,
            updatable = false,
            columnDefinition = "serial"
    )
    @Generated(event = EventType.INSERT)
    private Long id;

    private String nom;
    private String prenom;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CoachmarkState> coachmarkStates;
}