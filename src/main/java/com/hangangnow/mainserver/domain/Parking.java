package com.hangangnow.mainserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parking_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private Local local;
    private Address address;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    private Integer totalCount;
    private Integer availableCount;
}
