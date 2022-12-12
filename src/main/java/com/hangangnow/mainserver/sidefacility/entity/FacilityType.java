package com.hangangnow.mainserver.sidefacility.entity;

import java.util.Arrays;

public enum FacilityType {
    TOILET("TOILET"),
    SUN_SHADOW("SUN_SHADOW"),
    BICYCLE("BICYCLE"),
    STORE("STORE"),
    VIEW("VIEW"),
    DELIVERY_ZONE("DELIVERY_ZONE"),
    LOAD_FOOD("LOAD_FOOD"),
    BASKETBALL("BASKETBALL"),
    BASEBALL("BASEBALL"),
    SOCCER("SOCCER"),
    TENNIS("TENNIS"),
    SWIM("SWIM"),
    CAFE("CAFE"),
    RESTAURANT("RESTAURANT"),
    PARKING("PARKING");

    private final String name;

    FacilityType(String name) {
        this.name = name;
    }

    public static FacilityType getFacilityType(String type) {
        return Arrays.stream(values())
                .filter(facilityType -> facilityType.name.equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("주변시설 입력 값을 확인하세요"));
    }
}









