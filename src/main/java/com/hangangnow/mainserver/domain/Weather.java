package com.hangangnow.mainserver.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Weather {

    @Id
    @Column(name = "park_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "park_id")
    private Park park;

    private Double temperature;
    private Double windPower;
    private Double rainPercent;
    private Double Dust;
}
