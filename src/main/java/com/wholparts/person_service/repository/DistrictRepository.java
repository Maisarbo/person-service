package com.wholparts.person_service.repository;

import com.wholparts.person_service.model.City;
import com.wholparts.person_service.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DistrictRepository
        extends JpaRepository<District, UUID> {


    Optional<District> findByNameAndCity(String name, City city);

    List<District> findByCity(City city);
}