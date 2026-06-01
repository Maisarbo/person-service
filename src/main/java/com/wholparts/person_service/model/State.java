package com.wholparts.person_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "states")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String ibgeCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String Uf;

    @Builder.Default
    @OneToMany(mappedBy = "state")
    private List<City> cities = new ArrayList<>();
}
