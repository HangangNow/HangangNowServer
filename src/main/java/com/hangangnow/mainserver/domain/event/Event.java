package com.hangangnow.mainserver.domain.event;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.event.dto.EventRequestDto;
import com.hangangnow.mainserver.domain.mypage.scrap.EventScrap;
import com.hangangnow.mainserver.domain.mypage.scrap.Scrap;
import com.hangangnow.mainserver.domain.photo.EventPhoto;
import com.hangangnow.mainserver.domain.photo.ThumbnailPhoto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String title;

    private Local local;
    private Address address;

    private LocalDate startDate;
    private LocalDate endDate;
    private String eventTime;

    private String price;

    private String host;
    private String management;

    private String content;

    private LocalDateTime lastModifiedTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ThumbnailPhoto thumbnailPhoto;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private EventPhoto photo;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventScrap> eventScraps = new ArrayList<>();


    public Event(String title, Local local, Address address, LocalDate startDate, LocalDate endDate, String eventTime,
                 String price, String host, String management, String content,
                 ThumbnailPhoto thumbnailPhoto, EventPhoto photo, LocalDateTime lastModifiedTime) {
        this.title = title;
        this.local = local;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventTime = eventTime;
        this.price = price;
        this.host = host;
        this.management = management;
        this.content = content;
        this.thumbnailPhoto = thumbnailPhoto;
        this.photo = photo;
        this.lastModifiedTime = lastModifiedTime;
    }

    public void updateThumbnailPhoto(ThumbnailPhoto thumbnailPhoto){
        this.thumbnailPhoto = thumbnailPhoto;
    }


    public void updateEventPhoto(EventPhoto eventPhoto){
        this.photo = eventPhoto;
    }


    public void addEventScrap(EventScrap eventScrap){
        this.eventScraps.add(eventScrap);
    }

    public void deleteEventScrap(EventScrap eventScrap){
        this.eventScraps.remove(eventScrap);
    }


    public void update(EventRequestDto eventRequestDto, Local local, Address address){
        this.title = eventRequestDto.getTitle();
        this.local = local;
        this.address = address;
        this.startDate = LocalDate.parse(eventRequestDto.getStartDate(),DateTimeFormatter.ISO_DATE);
        this.endDate = LocalDate.parse(eventRequestDto.getEndDate(),DateTimeFormatter.ISO_DATE);
        this.eventTime = eventRequestDto.getEventTime();
        this.price = eventRequestDto.getPrice();
        this.host = eventRequestDto.getHost();
        this.management = eventRequestDto.getManagement();
        this.content = eventRequestDto.getContent();
    }

}
