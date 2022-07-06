package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.member.dto.*;
import com.hangangnow.mainserver.config.jwt.TokenProvider;
import com.hangangnow.mainserver.repository.MemberDataRepository;
import com.hangangnow.mainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final MemberDataRepository memberDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    public MemberSignupResponseDto signup(MemberSignupRequestDto memberSignupRequestDto){
        if (memberDataRepository.existsByLoginId(memberSignupRequestDto.getLoginId())){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member member = memberSignupRequestDto.toMember(passwordEncoder);
        return MemberSignupResponseDto.of(memberDataRepository.save(member));
    }

    public MemberLoginTokenDto login(MemberLoginRequestDto memberLoginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = memberLoginRequestDto.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }


    public boolean duplicateCheckByLoginId(MemberDuplicateDto memberDuplicateDto){
        if(memberRepository.findByLoginId(memberDuplicateDto.getLoginId()).isPresent()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean duplicateCheckByEmail(MemberDuplicateDto memberDuplicateDto){
        if(memberRepository.findByEmail(memberDuplicateDto.getEmail()).isPresent()){
            return true;
        }
        else{
            return false;
        }

    }

}
