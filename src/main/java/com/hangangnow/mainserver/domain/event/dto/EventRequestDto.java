package com.hangangnow.mainserver.domain.event.dto;

import lombok.Data;

@Data
public class EventRequestDto {
    private String si;
    private String gu;
    private String detail;

    private String title;
    private String period;
    private String time;

    private String price;
    private String content;

    private String host;
    private String management;
}
