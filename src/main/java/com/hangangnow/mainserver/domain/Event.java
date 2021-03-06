package com.hangangnow.mainserver.domain;

import com.hangangnow.mainserver.domain.photo.EventPhoto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String title;
    private Local local;
    private Address address;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;

    @OneToMany(mappedBy = "event")
    private List<EventPhoto> photos;
}
