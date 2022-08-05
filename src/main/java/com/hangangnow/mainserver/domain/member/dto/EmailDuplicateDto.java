package com.hangangnow.mainserver.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class EmailDuplicateDto {
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$" ,message = "올바른 이메일 형식을 입력하세요.")
    private String email;
}
