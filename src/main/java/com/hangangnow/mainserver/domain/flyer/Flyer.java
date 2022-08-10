package com.hangangnow.mainserver.domain.flyer;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.park.Park;
import com.hangangnow.mainserver.domain.photo.FlyerPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class Flyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private FlyerPhoto photo;

    @Embedded
    private Address address;

    private String call;


    public Flyer(String name, Park park, FlyerPhoto photo, Address address, String call) {
        this.name = name;
        this.park = park;
        this.photo = photo;
        this.address = address;
        this.call = call;
    }


    public void update(String name, FlyerPhoto photo, Address address, String call){
        this.name = name;
        this.photo = photo;
        this.address = address;
        this.call = call;
    }


    public void setPark(Park park){
        this.park = park;
    }
}
