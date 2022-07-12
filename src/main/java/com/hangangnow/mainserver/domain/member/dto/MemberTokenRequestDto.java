package com.hangangnow.mainserver.domain.member.dto;

import lombok.Data;

@Data
public class MemberTokenRequestDto {
    private String accessToken;
    private String refreshToken;

}
