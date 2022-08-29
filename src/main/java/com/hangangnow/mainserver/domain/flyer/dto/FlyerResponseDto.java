package com.hangangnow.mainserver.domain.flyer.dto;

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
    private String photoUrl;
    private String address;
    private String content;
    private String call;

    public FlyerResponseDto(Flyer flyer){
        this.id = flyer.getId();
        this.name = flyer.getName();
        this.parkName = flyer.getPark().getName();
        this.photoUrl = flyer.getPhoto().getUrl();
        this.address = flyer.getAddress().fullAddress();
        this.content = flyer.getContent();
        this.call = flyer.getCallNumber();
    }
}
