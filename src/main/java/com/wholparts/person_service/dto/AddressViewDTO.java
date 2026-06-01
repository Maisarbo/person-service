package com.wholparts.person_service.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressViewDTO {

    private UUID id;

    private String street;

    private String number;

    private String complement;

    private String zipCode;

    private String district;

    private String city;

    private String state;
}