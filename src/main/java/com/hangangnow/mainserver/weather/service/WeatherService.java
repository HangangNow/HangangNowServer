package com.hangangnow.mainserver.weather.service;

import com.hangangnow.mainserver.weather.entity.HangangNowData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final Long FIRST_INDEX = 1L;

    private final EntityManager em;

    public HangangNowData getHangangnowData(){
        return Optional.ofNullable(em.find(HangangNowData.class, FIRST_INDEX))
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNowData"));
    }
}
