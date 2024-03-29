package com.hangangnow.mainserver.sidefacility.dto;

import com.hangangnow.mainserver.sidefacility.entity.SideFacility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityResponseDto {

    private String name;
    private String address;
    private Double x_pos;
    private Double y_pos;

    public FacilityResponseDto(SideFacility sideFacility) {
        this.name = sideFacility.getLocal().getLocalname();
        this.address = sideFacility.getAddress().fullAddress();
        this.x_pos = sideFacility.getLocal().getX_pos();
        this.y_pos = sideFacility.getLocal().getY_pos();
    }
}
