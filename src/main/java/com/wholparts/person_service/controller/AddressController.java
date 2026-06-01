package com.wholparts.person_service.controller;

import com.wholparts.person_service.dto.*;
import com.wholparts.person_service.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/person/{personId}")
    public ResponseEntity<AddressViewDTO> addAddress(
            @PathVariable UUID personId,
            @RequestBody @Valid AddressCreationDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        addressService.addAddress(
                                personId,
                                dto
                        )
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressViewDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid AddressUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                addressService.update(id, dto)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressViewDTO> findById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                addressService.findById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {

        addressService.delete(id);

        return ResponseEntity.noContent().build();
    }
}