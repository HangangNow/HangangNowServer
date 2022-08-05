package com.hangangnow.mainserver.domain.member.dto;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.member.MemberMBTI;
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
    private MemberMBTI memberMBTI;
    private String photoUrl;

    public void setMemberResponseDto(Member member){
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.provider = member.getMemberProvider();
        this.memberMBTI = member.getMemberMBTI();

        if (member.getPhoto() == null){
            this.photoUrl = null;
        }
        else{
            this.photoUrl = member.getPhoto().getUrl();
        }

    }

}
