package com.hangangnow.mainserver.flyer.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FlyerRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    private String parkName;
    @NotBlank
    private String address;
    @NotBlank
    private String call;

    private String content;
}
