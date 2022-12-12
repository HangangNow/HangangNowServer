package com.hangangnow.mainserver.member.dto;

import com.hangangnow.mainserver.member.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberKakaoTokenDto {
    private String provider;
    private String email;
    private String name;
    private Gender gender;

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
