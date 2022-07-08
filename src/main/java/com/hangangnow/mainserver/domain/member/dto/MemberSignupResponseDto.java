package com.hangangnow.mainserver.domain.member.dto;

import com.hangangnow.mainserver.domain.member.Member;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignupResponseDto {
    private Long id;
    private String loginId;
    private String email;
    private String name;

    public static MemberSignupResponseDto of(Member member){
        return MemberSignupResponseDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

}
