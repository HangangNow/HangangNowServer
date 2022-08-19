package com.hangangnow.mainserver.domain.park.dto;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.park.Parking;
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
