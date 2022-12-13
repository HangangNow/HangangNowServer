package com.hangangnow.mainserver.sidefacility.entity;

import java.util.Arrays;

public enum Category {
    // "CS2", "PK6", "FD6", "CE7"
    CS2("CS2", "편의점"),
    PK6("PK6", "주차장"),
    FD6("FD6", "식당"),
    CE7("CE7", "카페");

    private final String type;
    private final String name;

    Category(String category, String name) {
        this.type = category;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public static Category getCategory(String type) {
        return Arrays.stream(values())
                .filter(category -> category.type.equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 코드 입니다"));
    }
}
