package com.hangangnow.mainserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Whether {

    @Id
    @GeneratedValue
    @Column(name = "whether_id")
    private Long id;
    private Park park;
    private Double temperature;
    private Double windPower;
    private Double rainPercent;
    private Double Dust;
}
