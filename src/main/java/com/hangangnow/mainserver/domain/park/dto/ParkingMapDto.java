package com.hangangnow.mainserver.domain.park.dto;

import com.hangangnow.mainserver.domain.common.Local;
import com.hangangnow.mainserver.domain.park.Parking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingMapDto {
    private Long id;
    private Local local;

    public ParkingMapDto(Parking parking){
        this.id = parking.getId();
        this.local = parking.getLocal();
    }
}

