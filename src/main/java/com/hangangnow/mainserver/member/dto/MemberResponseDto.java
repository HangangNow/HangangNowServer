package com.hangangnow.mainserver.member.dto;

import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.member.entity.MemberMBTI;
import com.hangangnow.mainserver.member.entity.MemberProvider;
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
    private Boolean marketing_agree;
    private Boolean alarm_agree;

    public void setMemberResponseDto(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.provider = member.getMemberProvider();
        this.memberMBTI = member.getMemberMBTI();
        this.marketing_agree = member.getMarketing_agree();
        this.alarm_agree = member.getAlarm_agree();

        if (member.getPhoto() == null) {
            this.photoUrl = null;
        } else {
            this.photoUrl = member.getPhoto().getUrl();
        }

    }

}
