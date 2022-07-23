package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.MemberAuthenticationProvider;
import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.member.MemberProvider;
import com.hangangnow.mainserver.domain.member.RefreshToken;
import com.hangangnow.mainserver.domain.member.dto.*;
import com.hangangnow.mainserver.config.jwt.TokenProvider;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberAuthenticationProvider memberAuthenticationProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MailService mailService;


    @Transactional
    public MemberResponseDto signup(MemberSignupRequestDto memberSignupRequestDto){
        if (memberRepository.findByLoginId(memberSignupRequestDto.getLoginId()).isPresent()){
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        Member member = memberSignupRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }


    @Transactional
    public MemberTokenDto login(MemberLoginRequestDto memberLoginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = memberLoginRequestDto.toAuthentication();

        Authentication authentication = memberAuthenticationProvider.authenticate(authenticationToken);

        MemberTokenDto memberTokenDto = tokenProvider.generateTokenDto(authentication, memberLoginRequestDto.getAutoLogin());

        RefreshToken refreshToken = RefreshToken.builder()
                .key(Long.parseLong(authentication.getName()))
                .value(memberTokenDto.getRefreshToken())
                .build();



        memberTokenDto.setProvider("GENERAL");

        refreshTokenRepository.save(refreshToken);

        return memberTokenDto;
    }


    @Transactional
    public MemberTokenDto reissue(MemberTokenRequestDto memberTokenRequestDto){
        // 1. Refresh Token 검증
        if(!tokenProvider.validateToken(memberTokenRequestDto.getRefreshToken())){
            throw new IllegalArgumentException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 member 가져오기
        Authentication authentication = tokenProvider.getAuthentication(memberTokenRequestDto.getAccessToken());

        // 3. 저장소에서 memberId 기반으로 RefreshToken 객체 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(memberTokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        MemberTokenDto memberTokenDto = tokenProvider.generateTokenDto(authentication, memberTokenRequestDto.getAutoLogin());
        refreshToken.updateValue(memberTokenRequestDto.getRefreshToken());

        return memberTokenDto;
    }


    public boolean duplicateCheckByLoginId(MemberDuplicateDto memberDuplicateDto){
        if(memberRepository.findByLoginId(memberDuplicateDto.getLoginId()).isPresent()){
            return true;
        }
        else{
            return false;
        }
    }


    public ResponseDto duplicateCheckByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElse(new Member());

        if (member.getId() == null){
            return new ResponseDto("0, 가입된 이메일이 존재하지 않습니다.");
        }

        else{
            if(member.getMemberProvider() == MemberProvider.KAKAO){
                return new ResponseDto("1, 카카오 로그인으로 가입된 이메일입니다. 카카오 로그인을 이용하세요");
            }

            else{
                return new ResponseDto("2, 이미 가입된 이메일입니다.");
            }
        }
    }

    @Transactional
    public ResponseDto changePassword(PasswordRequestDto passwordRequestDto) {
        Member member = memberRepository.findByLoginId(passwordRequestDto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다."));

        String password1 = passwordRequestDto.getPassword1();
        String password2 = passwordRequestDto.getPassword2();

        if (!password1.equals(password2)){
            throw new IllegalArgumentException("비밀번호가 서로 일치하지 않습니다");
        }

        if (passwordEncoder.matches(password1, member.getPassword())){
            throw new IllegalArgumentException("기존 비밀번호와 일치합니다.");
        }

        member.updatePassword(passwordEncoder.encode(password1));

        return new ResponseDto("비밀번호가 정상적으로 변경되었습니다.");
    }

    public ResponseDto findLoginIdByEmail(LoginIdRequestDto loginIdRequestDto) {
        Member findMember = memberRepository.findByEmail(loginIdRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 아이디가 존재하지 않습니다"));

        mailService.authLoginId(loginIdRequestDto.getEmail(), loginIdRequestDto.getName(), findMember.getLoginId());
        return new ResponseDto(loginIdRequestDto.getName() + "님 이메일로 아이디가 전송되었습니다.");
    }
}
