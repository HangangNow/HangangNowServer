package com.hangangnow.mainserver.domain.event;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.event.dto.EventRequestDto;
import com.hangangnow.mainserver.domain.photo.EventPhoto;
import com.hangangnow.mainserver.domain.photo.ThumbnailPhoto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
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

    private String period;
    private String eventTime;

    private String price;

    private String host;
    private String management;

    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ThumbnailPhoto thumbnailPhoto;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private EventPhoto photo;

    public Event(String title, Local local, Address address, String period, String eventTime,
                 String price, String host, String management, String content,
                 ThumbnailPhoto thumbnailPhoto, EventPhoto photo) {
        this.title = title;
        this.local = local;
        this.address = address;
        this.period = period;
        this.eventTime = eventTime;
        this.price = price;
        this.host = host;
        this.management = management;
        this.content = content;
        this.thumbnailPhoto = thumbnailPhoto;
        this.photo = photo;
    }


//    public void update(EventRequestDto eventRequestDto, ){
//        this.title = event.getTitle();
//        this.local = event.getLocal();
//        this.address = event.getAddress();
//        this.period = event.getPeriod();
//        this.eventTime = event.getEventTime();
//        this.price = event.getPrice();
//        this.host =
//        this.management =
//        this.content =
//        this.thumbnailPhoto =
//        this.photo =
//    }

}
