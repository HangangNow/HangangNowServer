package com.hangangnow.mainserver.sidefacility.entity;

import com.hangangnow.mainserver.common.entity.Address;
import com.hangangnow.mainserver.common.entity.Local;
import com.hangangnow.mainserver.park.entity.Park;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class SideFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Embedded
    private Local local;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    @Enumerated(EnumType.STRING)
    private FacilityType facilityType;

    public SideFacility(Address address, Local local, Park park, FacilityType facilityType) {
        this.address = address;
        this.local = local;
        this.park = park;
        this.facilityType = facilityType;
    }
}
