package com.wholparts.person_service.dto;

import com.wholparts.person_service.enums.PersonType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonCreationDTO {

    @NotBlank
    private String name;

    private String tradeName;

    @NotBlank
    private String document;

    private String email;

    private String phone;

    private PersonType type;

    @Valid
    @NotEmpty
    private List<AddressCreationDTO> addresses;

}