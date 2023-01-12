package com.hangangnow.mainserver.picnic.dto;

import com.hangangnow.mainserver.common.entity.Address;
import com.hangangnow.mainserver.common.entity.Local;
import com.hangangnow.mainserver.picnic.entity.RecomPlace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class RecomPlaceResponseDto {

    private Long id;

    private String name;

    private Address address;

    private Local local;

    private Boolean isScrap;

    public RecomPlaceResponseDto(RecomPlace recomPlace) {
        this.id = recomPlace.getId();
        this.name = recomPlace.getName();
        this.address = recomPlace.getAddress();
        this.local = recomPlace.getLocal();
    }

    public void setIsScrap(Boolean isScrap) {
        this.isScrap = isScrap;
    }
}
