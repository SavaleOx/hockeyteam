package com.savaleox.hockeyteam.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coaches")
@Getter
@Setter
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    private Integer age;

    @Column
    private String tactic;

    @OneToOne
    @JoinColumn(name = "team_id", unique = true)
    private Team team;
}