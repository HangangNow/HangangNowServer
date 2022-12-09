package com.hangangnow.mainserver.domain.picnic.dto;

import com.hangangnow.mainserver.domain.common.Address;
import com.hangangnow.mainserver.domain.common.Local;
import com.hangangnow.mainserver.domain.picnic.RecomPlace;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RecomPlaceResponseDto {

    private Long id;

    private String name;

    private Address address;

    private Local local;

    private Boolean isScrap;

    public RecomPlaceResponseDto(RecomPlace recomPlace){
        this.id = recomPlace.getId();
        this.name = recomPlace.getName();
        this.address = recomPlace.getAddress();
        this.local = recomPlace.getLocal();
    }

    public void setIsScrap(Boolean isScrap){
        this.isScrap = isScrap;
    }
}
