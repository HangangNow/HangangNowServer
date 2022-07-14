package com.hangangnow.mainserver.domain;

import com.hangangnow.mainserver.domain.photo.ParkPhoto;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "park_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    private Local local;

    @Embedded
    private Address address;

    private String content;

    @OneToMany(mappedBy = "park")
    private List<ParkPhoto> photos = new ArrayList<>();

    public Park() {}

    public Park(String name, Local local, Address address, String content) {
        this.name = name;
        this.local = local;
        this.address = address;
        this.content = content;
    }
}
