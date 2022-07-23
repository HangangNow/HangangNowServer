package com.hangangnow.mainserver.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberKakaoDto {
    private Long kakaoId;
    private String loginId;
    private String email;
    private String name;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, name);
    }
}
