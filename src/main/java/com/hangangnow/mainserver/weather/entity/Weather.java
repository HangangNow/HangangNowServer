package com.hangangnow.mainserver.weather.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class Weather {

    private Integer skyMode;
    private Integer maxRainPercent;

    public Weather(Integer skyMode, Integer maxRainPercent){
        this.skyMode = skyMode;
        this.maxRainPercent = maxRainPercent;
    }

}