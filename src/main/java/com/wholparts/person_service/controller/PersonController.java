package com.wholparts.person_service.controller;

import com.wholparts.person_service.dto.PersonCreationDTO;
import com.wholparts.person_service.dto.PersonUpdateDTO;
import com.wholparts.person_service.dto.PersonViewDTO;
import com.wholparts.person_service.enums.PersonStatus;
import com.wholparts.person_service.enums.PersonType;
import com.wholparts.person_service.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<PersonViewDTO> create(
            @RequestBody
            @Valid
            PersonCreationDTO dto
    ) {

        PersonViewDTO response =
                personService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonViewDTO> update(
            @PathVariable UUID id,
            @RequestBody
            @Valid
            PersonUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                personService.update(id, dto)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonViewDTO> findById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                personService.findById(id)
        );
    }

    @GetMapping("/document/{document}")
    public ResponseEntity<PersonViewDTO> findByDocument(
            @PathVariable String document
    ) {

        return ResponseEntity.ok(
                personService.findByDocument(document)
        );
    }

    @GetMapping
    public ResponseEntity<Page<PersonViewDTO>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                personService.findAllPaged(page, size)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PersonViewDTO>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) PersonStatus status,
            @RequestParam(required = false) PersonType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                personService.search(
                        name,
                        status,
                        type,
                        page,
                        size
                )
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable UUID id
    ) {

        personService.activate(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable UUID id
    ) {

        personService.deactivate(id);

        return ResponseEntity.noContent().build();
    }
}