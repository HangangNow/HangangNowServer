package com.hangangnow.mainserver.domain.member.dto;

import com.hangangnow.mainserver.domain.member.Authority;
import com.hangangnow.mainserver.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSignupRequestDto {
    private String loginId;
    private String password;
    private String email;
    private String name;
    private String birthday;
    private String gender;


    public Member toMember(PasswordEncoder passwordEncoder) {
        if ("MALE".equals(gender)){
            return Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .gender(Gender.MALE)
                    .birthday(LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE))
                    .authority(Authority.ROLE_USER)
                    .build();
        }
        else{
            return Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .gender(Gender.FEMALE)
                    .birthday(LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE))
                    .authority(Authority.ROLE_USER)
                    .build();
        }
    }
}
