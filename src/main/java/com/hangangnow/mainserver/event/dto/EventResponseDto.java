package com.hangangnow.mainserver.event.dto;


import com.hangangnow.mainserver.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class EventResponseDto {
    private Long id;
    private String title;
    private String address;
    private Double x_pos;
    private Double y_pos;
    private LocalDate startDate;
    private LocalDate endDate;
    private String eventTime;
    private String price;
    private String content;
    private String host;
    private String management;
    private String thumbnailUrl;
    private String photoUrl;
    private LocalDateTime lastModifiedTime;
    private Boolean isScrap;

    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.address = event.getAddress().fullAddress();
        this.x_pos = event.getLocal().getX_pos();
        this.y_pos = event.getLocal().getY_pos();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.eventTime = event.getEventTime();
        this.price = event.getPrice();
        this.content = event.getContent();
        this.host = event.getHost();
        this.management = event.getManagement();
        this.thumbnailUrl = event.getThumbnailPhoto().getUrl();
        this.photoUrl = event.getPhoto().getUrl();
        this.lastModifiedTime = event.getLastModifiedTime();
    }

    public void setIsScrap(Boolean isScrap){
        this.isScrap = isScrap;
    }

}
