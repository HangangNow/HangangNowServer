package com.hangangnow.mainserver.sidefacility.dto;

import lombok.Data;

@Data
public class FacilityRequestDto {
    private String address;
    private Double x_pos;
    private Double y_pos;
    private String name;
    private String parkName;
    private String type;

}
