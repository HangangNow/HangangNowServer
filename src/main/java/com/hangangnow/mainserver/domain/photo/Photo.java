package com.hangangnow.mainserver.domain.photo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "photo_category")
@Getter
public class Photo {

    @Id
    @GeneratedValue
    @Column(name = "photo_id")
    private Long id;
    private String url;
}
