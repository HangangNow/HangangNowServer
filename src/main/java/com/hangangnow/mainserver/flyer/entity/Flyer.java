package com.hangangnow.mainserver.flyer.entity;

import com.hangangnow.mainserver.common.entity.Address;
import com.hangangnow.mainserver.scrap.entity.FlyerScrap;
import com.hangangnow.mainserver.park.entity.Park;
import com.hangangnow.mainserver.photo.entity.FlyerPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private String content;

    private String callNumber;

    @OneToMany(mappedBy = "flyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlyerScrap> flyerScraps = new ArrayList<>();


    public Flyer(String name, FlyerPhoto photo, Address address, String content, String callNumber) {
        this.name = name;
        this.photo = photo;
        this.address = address;
        this.content = content;
        this.callNumber = callNumber;
    }


    public void update(String name, Address address, String content, String call) {
        this.name = name;
        this.address = address;
        this.callNumber = call;
        this.content = content;
    }

    public void updatePhoto(FlyerPhoto flyerPhoto) {
        this.photo = flyerPhoto;
    }


    public void setPark(Park park) {
        if (this.park != null) {
            this.park.getFlyers().remove(this);
        }

        this.park = park;
    }


    public void addFlyerScrap(FlyerScrap flyerScrap) {
        this.flyerScraps.add(flyerScrap);
    }


    public void deleteFlyerScrap(FlyerScrap flyerScrap) {
        this.flyerScraps.remove(flyerScrap);
    }
}
