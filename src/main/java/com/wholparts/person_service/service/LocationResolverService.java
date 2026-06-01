package com.wholparts.person_service.service;

import com.wholparts.person_service.model.City;
import com.wholparts.person_service.model.District;
import com.wholparts.person_service.repository.CityRepository;
import com.wholparts.person_service.repository.DistrictRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationResolverService {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;

    public LocationResolverService(
            CityRepository cityRepository,
            DistrictRepository districtRepository
    ) {
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
    }

    public City resolveCity(Integer ibgeCode) {

        return cityRepository.findByIbgeCode(ibgeCode)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Cidade não encontrada para IBGE: "
                                        + ibgeCode
                        )
                );
    }

    public District resolveDistrict(
            String name,
            String code,
            City city
    ) {

        return districtRepository.findByNameAndCity(name, city)
                .orElseGet(() -> {

                    District d = new District();

                    d.setCode(code);
                    d.setName(name);
                    d.setCity(city);

                    return districtRepository.save(d);
                });
    }
}
