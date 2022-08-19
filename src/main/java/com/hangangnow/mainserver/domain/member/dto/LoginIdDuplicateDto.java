package com.hangangnow.mainserver.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginIdDuplicateDto {
    @NotBlank(message = "아이디는 필수 값 입니다.")
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "아이디는 5~20자의 영문 소문자, 숫자만 사용 가능합니다.")
    private String loginId;
}
