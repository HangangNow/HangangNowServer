package com.hangangnow.mainserver.domain.flyer.dto;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.flyer.Flyer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlyerResponseDto {
    private Long id;
    private String name;
    private String parkName;
    private String url;
    private Address address;
    private String call;

    public FlyerResponseDto(Flyer flyer){
        this.id = flyer.getId();
        this.name = flyer.getName();
        this.parkName = flyer.getPark().getName();
        this.url = flyer.getPhoto().getUrl();
        this.address = flyer.getAddress();
        this.call = flyer.getCall();
    }
}
