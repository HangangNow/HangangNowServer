package com.hangangnow.mainserver.domain.member.dto;

import lombok.Data;

@Data
public class MemberDuplicateDto {
    private String loginId;
    private String email;
}
