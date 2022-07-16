package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class WeatherRepository {

    private final EntityManager em;

    public Weather findOne(Long id){
        return em.find(Weather.class, id);
    }
}
