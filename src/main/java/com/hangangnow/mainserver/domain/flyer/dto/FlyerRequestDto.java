package com.hangangnow.mainserver.domain.flyer.dto;

import lombok.Data;


@Data
public class FlyerRequestDto {
    private String name;
    private String parkName;
    private String address;
    private String call;
}
