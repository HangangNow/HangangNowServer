package com.hangangnow.mainserver.domain.scrap.dto;

import com.hangangnow.mainserver.domain.picnic.RecomPlace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomPlaceScrapDto {

    private Long id;
    private String name;
    private String address;
    private Double x_pos;
    private Double y_pos;


    public RecomPlaceScrapDto(RecomPlace recomPlace) {
        this.id = recomPlace.getId();
        this.name = recomPlace.getName();
        this.address = recomPlace.getAddress().fullAddress();
        this.x_pos = recomPlace.getLocal().getX_pos();
        this.y_pos = recomPlace.getLocal().getY_pos();
    }
}
