package com.hangangnow.mainserver.park.dto;

import com.hangangnow.mainserver.common.entity.Local;
import com.hangangnow.mainserver.park.entity.Parking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingMapDto {

    private Long id;
    private Local local;

    public ParkingMapDto(Parking parking) {
        this.id = parking.getId();
        this.local = parking.getLocal();
    }
}

