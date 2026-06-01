package com.wholparts.person_service.service;

import com.wholparts.person_service.enums.PersonType;
import com.wholparts.person_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonCodeService {

    private final PersonRepository repository;

    public String generate(PersonType type) {

        String prefix = type.getPrefix();

        Long count = repository.countByType(type);

        long next = count + 1;

        return prefix + String.format("%04d", next);
    }
}