package com.hangangnow.mainserver.domain.park.dto;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.park.Parking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingResponseDto {
    private Long id;
    private String name;
    private Address address;
    private Local local;

    private int total_count;
    private int available_count;
    //기본 30분, 간격 10분 기준
    private String basicCharge;
    private String intervalCharge;
    private String fulldayCharge;

    public ParkingResponseDto(Parking parking){
        this.id = parking.getId();
        this.name = parking.getName();
        this.address = parking.getAddress();
        this.local = parking.getLocal();
        this.total_count = parking.getTotal_count();
        this.available_count = parking.getAvailable_count();
        this.basicCharge = parking.getBasicCharge();
        this.intervalCharge = parking.getIntervalCharge();
        this.fulldayCharge = parking.getFulldayCharge();
    }
}
