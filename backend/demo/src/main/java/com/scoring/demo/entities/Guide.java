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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guide")
public class Guide {

    public Guide(String nom) {
        this.nom = nom;
    }

    @Id
    @GeneratedValue
    private UUID uuid;

    @SequenceGenerator(
            name = "guide_id_seq_gen",
            sequenceName = "guide_id_seq",
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

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    private List<CoachmarkState> coachmarkStates;
}