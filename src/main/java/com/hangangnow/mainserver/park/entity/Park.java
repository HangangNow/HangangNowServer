package com.hangangnow.mainserver.park.entity;

import com.hangangnow.mainserver.common.entity.Address;
import com.hangangnow.mainserver.common.entity.Local;
import com.hangangnow.mainserver.flyer.entity.Flyer;
import com.hangangnow.mainserver.photo.entity.ParkPhoto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "park_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String contentId;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    private Local local;

    @Embedded
    private Address address;

    private String callNumber;

    private String facilities;

    private String funItems;

    private String summary;

    private String content;

    private LocalDateTime lastModifiedTime;

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkPhoto> photos = new ArrayList<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flyer> flyers = new ArrayList<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parking> parkings = new ArrayList<>();


    public Park(String name, Local local, Address address, String content, LocalDateTime lastModifiedTime) {
        this.name = name;
        this.local = local;
        this.address = address;
        this.content = content;
        this.lastModifiedTime = lastModifiedTime;
    }


    public void addFlyer(Flyer flyer) {
        this.flyers.add(flyer);
        flyer.setPark(this);
    }

}
