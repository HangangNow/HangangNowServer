package com.hangangnow.mainserver.domain.sidefacility.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityKakaoResponseDto {

    private String name;
    private String address;
    private String call;
    private String kakaoInfoUrl;
    private Double distance;
    private Double x_pos;
    private Double y_pos;
}
