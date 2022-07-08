package com.hangangnow.mainserver.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginTokenDto {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
}
