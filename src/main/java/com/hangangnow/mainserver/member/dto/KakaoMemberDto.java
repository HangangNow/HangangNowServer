package com.hangangnow.mainserver.member.dto;

import com.hangangnow.mainserver.member.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMemberDto {
    private Long kakaoId;
    private String loginId;
    private String email;
    private String name;
    private Gender gender;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, name);
    }
}
