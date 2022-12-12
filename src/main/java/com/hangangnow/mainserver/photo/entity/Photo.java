package com.hangangnow.mainserver.photo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "photo_category")
@Getter
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    protected Long id;

    @Column(nullable = false)
    protected String url;

    protected LocalDateTime lastModifiedTime;


}
