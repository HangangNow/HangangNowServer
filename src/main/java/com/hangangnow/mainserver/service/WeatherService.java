package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.domain.Weather;
import com.hangangnow.mainserver.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public Weather findOne(Long id) {
        return weatherRepository.findOne(id);
    }
}
