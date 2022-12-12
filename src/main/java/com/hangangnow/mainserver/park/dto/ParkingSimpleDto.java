package com.hangangnow.mainserver.park.dto;

import com.hangangnow.mainserver.common.entity.Address;
import com.hangangnow.mainserver.park.entity.Parking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSimpleDto {

    private Long id;
    private String name;
    private Address address;

    public ParkingSimpleDto(Parking parking){
        this.id = parking.getId();
        this.name = parking.getName();
        this.address = parking.getAddress();
    }
}
