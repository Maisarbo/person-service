package com.wholparts.person_service.dto;

import com.wholparts.person_service.enums.PersonStatus;
import com.wholparts.person_service.enums.PersonType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonViewDTO {

    private UUID id;

    private String code;

    private String name;

    private String tradeName;

    private String document;

    private String email;

    private String phone;

    private PersonType type;

    private PersonStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<AddressViewDTO> addresses;
}