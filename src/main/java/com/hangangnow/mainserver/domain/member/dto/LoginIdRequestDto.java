package com.hangangnow.mainserver.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class LoginIdRequestDto {
    private String name;
    @Email
    private String email;
}
