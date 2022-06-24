package com.hangangnow.mainserver.domain;

import com.hangangnow.mainserver.domain.photo.ParkPhoto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Park {

    @Id
    @GeneratedValue
    @Column(name = "park_id")
    private Long id;

    private String name;

    @Embedded
    private Local local;

    @Embedded
    private Address address;

    private String describe;

    @OneToMany(mappedBy = "park")
    private Parking parking;

    @OneToMany(mappedBy = "park")
    private List<ParkPhoto> photos = new ArrayList<>();

}
