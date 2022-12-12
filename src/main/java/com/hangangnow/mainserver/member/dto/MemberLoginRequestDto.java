package com.hangangnow.mainserver.member.dto;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberLoginRequestDto {

    @NotBlank(message = "아이디는 필수 값 입니다.")
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "아이디는 5~20자의 영문 소문자, 숫자만 사용 가능합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,15}", message = "비밀번호는 영문 대,소문자와 숫자가 포함된 8자 ~ 15자의 비밀번호입니다.")
    private String password;

    private Boolean autoLogin;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(loginId, password);
    }
}
