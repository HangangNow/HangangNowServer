package com.hangangnow.mainserver.domain.sidefacility.dto;

import com.hangangnow.mainserver.domain.sidefacility.SideFacility;
import lombok.Data;

@Data
public class FacilityResponseDto {
    private String name;
    private String address;
    private Double x_pos;
    private Double y_pos;

    public FacilityResponseDto (SideFacility sideFacility){
        this.name = sideFacility.getLocal().getLocalname();
        this.address = sideFacility.getAddress().fullAddress();
        this.x_pos = sideFacility.getLocal().getX_pos();
        this.y_pos = sideFacility.getLocal().getY_pos();
    }
}
