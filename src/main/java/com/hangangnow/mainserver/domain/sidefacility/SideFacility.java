package com.hangangnow.mainserver.domain.sidefacility;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.member.MemberMBTI;
import com.hangangnow.mainserver.domain.park.Park;
import lombok.AllArgsConstructor;
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

    public void setSideFacility(Address address,  Local local, Park park, String type){
        this.address = address;
        this.local = local;
        this.park = park;
        this.facilityType = getType(type);
    }


    private FacilityType getType(String type) {
        // TOILET, SUN_SHADOW, BICYCLE, STORE, VIEW, DELIVERY_ZONE,
        // LOAD_FOOD, BASKETBALL, SOCCER, TENNIS, SWIM
        switch (type){
            case "TOILET":
                return FacilityType.TOILET;
            case "SUN_SHADOW":
                return FacilityType.SUN_SHADOW;
            case "BICYCLE":
                return FacilityType.BICYCLE;
            case "STORE":
                return FacilityType.STORE;
            case "CAFE":
                return FacilityType.CAFE;
            case "RESTAURANT":
                return FacilityType.RESTAURANT;
            case "PARKING":
                return FacilityType.PARKING;
            case "VIEW":
                return FacilityType.VIEW;
            case "DELIVERY_ZONE":
                return FacilityType.DELIVERY_ZONE;
            case "LOAD_FOOD":
                return FacilityType.LOAD_FOOD;
            case "BASKETBALL":
                return FacilityType.BASKETBALL;
            case "SOCCER":
                return FacilityType.SOCCER;
            case "TENNIS":
                return FacilityType.TENNIS;
            case "SWIM":
                return FacilityType.SWIM;
            case "BASEBALL":
                return FacilityType.BASEBALL;
            default:
                return null;
        }
    }
}
