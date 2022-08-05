package com.hangangnow.mainserver.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthDto {
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String email;
    private String code;
}
