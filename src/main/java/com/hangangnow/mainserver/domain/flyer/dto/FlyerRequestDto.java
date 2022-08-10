package com.hangangnow.mainserver.domain.flyer.dto;

import lombok.Data;


@Data
public class FlyerRequestDto {
    private String name;
    private String parkName;
    private String si;
    private String gu;
    private String detail;
    private String call;
}
