package com.hangangnow.mainserver.domain.sidefacility;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.park.Park;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class SideFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Address address;

    private Local local;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    @Enumerated(EnumType.STRING)
    private FacilityType facilityType;
}
