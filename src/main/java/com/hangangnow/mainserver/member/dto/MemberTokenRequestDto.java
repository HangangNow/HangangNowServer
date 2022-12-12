package com.hangangnow.mainserver.member.dto;

import lombok.Data;

@Data
public class MemberTokenRequestDto {

    private String accessToken;
    private String refreshToken;
    private Boolean autoLogin;

}
