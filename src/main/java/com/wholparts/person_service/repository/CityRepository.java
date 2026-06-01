package com.wholparts.person_service.repository;

import com.wholparts.person_service.model.City;
import com.wholparts.person_service.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CityRepository
        extends JpaRepository<City, UUID> {

    Optional<City> findByIbgeCode(Integer ibgeCode);

    List<City> findByState(State state);
}