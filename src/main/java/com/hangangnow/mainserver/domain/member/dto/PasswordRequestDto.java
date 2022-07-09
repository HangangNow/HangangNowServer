package com.hangangnow.mainserver.domain.member.dto;

import lombok.Data;

@Data
public class PasswordRequestDto {
    private String loginId;
    private String password1;
    private String password2;
}
