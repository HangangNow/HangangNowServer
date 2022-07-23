package com.hangangnow.mainserver.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PasswordRequestDto {
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,15}", message = "비밀번호는 영문 대,소문자와 숫자가 포함된 8자 ~ 15자의 비밀번호입니다.")
    private String password1;
    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,15}", message = "비밀번호는 영문 대,소문자와 숫자가 포함된 8자 ~ 15자의 비밀번호입니다.")
    private String password2;
}
