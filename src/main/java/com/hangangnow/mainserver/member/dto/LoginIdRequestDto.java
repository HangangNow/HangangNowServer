package com.hangangnow.mainserver.member.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginIdRequestDto {
    @NotBlank(message = "1자 이상의 이름을 입력하세요.")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$" ,message = "올바른 이메일 형식을 입력하세요.")
    private String email;
}
