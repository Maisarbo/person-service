package com.wholparts.person_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String number;

    private String complement;

    @Column(nullable = false)
    private String zipCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Person person;
}