package com.hangangnow.mainserver.domain.event.dto;


import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class EventResponseDto {
    private String title;
    private String address;
    private Double x_pos;
    private Double y_pos;
    private String period;
    private String eventTime;
    private String price;
    private String content;
    private String host;
    private String management;
    private String thumbnailUrl;
    private String photoUrl;

    public EventResponseDto(Event event) {
        this.title = event.getTitle();
        this.address = event.getAddress().fullAddress();
        this.x_pos = event.getLocal().getX_pos();
        this.y_pos = event.getLocal().getY_pos();
        this.period = event.getPeriod();
        this.eventTime = event.getEventTime();
        this.price = event.getPrice();
        this.content = event.getContent();
        this.host = event.getHost();
        this.management = event.getManagement();
        this.thumbnailUrl = event.getThumbnailPhoto().getUrl();
        this.photoUrl = event.getPhoto().getUrl();

    }


}
