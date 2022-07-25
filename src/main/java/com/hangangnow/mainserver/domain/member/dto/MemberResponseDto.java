package com.hangangnow.mainserver.domain.member.dto;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.member.MemberProvider;
import lombok.*;

import java.util.UUID;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private UUID id;
    private String loginId;
    private String email;
    private String name;
    private MemberProvider provider;

    public static MemberResponseDto of(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .name(member.getName())
                .provider(member.getMemberProvider())
                .build();
    }

}
