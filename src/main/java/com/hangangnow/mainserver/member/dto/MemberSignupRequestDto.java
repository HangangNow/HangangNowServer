package com.hangangnow.mainserver.member.dto;

import com.hangangnow.mainserver.member.entity.Authority;
import com.hangangnow.mainserver.member.entity.Gender;
import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.member.entity.MemberProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSignupRequestDto {

    @NotBlank(message = "아이디는 필수 값 입니다.")
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "아이디는 5~20자의 영문 소문자, 숫자만 사용 가능합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,15}", message = "비밀번호는 영문 대,소문자와 숫자가 포함된 8자 ~ 15자의 비밀번호입니다.")
    private String password;

    @Email(message = "올바른 이메일 형식을 입력하세요")
    private String email;
    @NotBlank(message = "이름은 필수 값 입니다.")
    private String name;

    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "생년월일은 yyyy-mm-dd 타입입니다.")
    private String birthday;
    private String gender;


    public Member toMember(PasswordEncoder passwordEncoder) {
        if ("MALE".equals(gender)) {
            return Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .gender(Gender.MALE)
                    .birthday(LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE))
                    .authority(Authority.ROLE_USER)
                    .memberProvider(MemberProvider.GENERAL)
                    .marketing_agree(false)
                    .alarm_agree(false)
                    .build();
        } else {
            return Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .gender(Gender.FEMALE)
                    .birthday(LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE))
                    .authority(Authority.ROLE_USER)
                    .memberProvider(MemberProvider.GENERAL)
                    .marketing_agree(false)
                    .alarm_agree(false)
                    .build();
        }
    }
}
