package com.hangangnow.mainserver.weather.service;

import com.hangangnow.mainserver.weather.entity.HangangNowData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HangangNowService {
    private final EntityManager em;

    public HangangNowData hangangNowData(){
        return Optional.ofNullable(em.find(HangangNowData.class, 1L))
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNowData"));
    }
}
