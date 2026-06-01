package com.wholparts.person_service.service;

import com.wholparts.person_service.client.IbgeClient;
import com.wholparts.person_service.dto.IbgeCityDTO;
import com.wholparts.person_service.dto.IbgeStateDTO;
import com.wholparts.person_service.model.City;
import com.wholparts.person_service.model.State;
import com.wholparts.person_service.repository.CityRepository;
import com.wholparts.person_service.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IbgeImportService {

    private final IbgeClient ibgeClient;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    public IbgeImportService(IbgeClient ibgeClient, StateRepository stateRepository, CityRepository cityRepository) {
        this.ibgeClient = ibgeClient;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
    }

    public void importAll() {

        if (stateRepository.count() > 0) {
            return; // já importado
        }

        List<IbgeStateDTO> states = ibgeClient.fetchStates();

        for (IbgeStateDTO s : states) {

            State state = new State();
            state.setIbgeCode(String.valueOf(s.getId()));
            state.setName(s.getNome());
            state.setUf(s.getSigla());

            state = stateRepository.save(state);

            List<IbgeCityDTO> cities =
                    ibgeClient.fetchCities(s.getSigla());

            for (IbgeCityDTO c : cities) {

                City city = new City();
                city.setIbgeCode(c.getId());
                city.setName(c.getNome());
                city.setState(state);

                cityRepository.save(city);
            }
        }
    }
}
