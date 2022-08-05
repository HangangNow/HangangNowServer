package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.security.MemberAuthenticationProvider;

import com.hangangnow.mainserver.config.redis.RedisUtil;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.member.MemberProvider;
import com.hangangnow.mainserver.domain.member.dto.*;
import com.hangangnow.mainserver.config.jwt.TokenProvider;
import com.hangangnow.mainserver.domain.photo.MemberPhoto;
import com.hangangnow.mainserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final RedisUtil redisUtil;
    private final MemberAuthenticationProvider memberAuthenticationProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MailService mailService;

    private static final long REFRESH_TOKEN_TTL = 60 * 60 * 24 * 7;  // 7일(s)
    private static final long REFRESH_TOKEN_AUTOLOGIN_TTL = 60 * 60 * 24 * 90;  // 90일(s)


    @Transactional
    public MemberResponseDto signup(MemberSignupRequestDto memberSignupRequestDto){
        if (memberRepository.findByLoginId(memberSignupRequestDto.getLoginId()).isPresent()){
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        Member member = memberSignupRequestDto.toMember(passwordEncoder);

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberResponseDto(memberRepository.save(member));

        return memberResponseDto;
    }


    @Transactional
    public MemberTokenDto login(MemberLoginRequestDto memberLoginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = memberLoginRequestDto.toAuthentication();

        Authentication authentication = memberAuthenticationProvider.authenticate(authenticationToken);

        MemberTokenDto memberTokenDto = tokenProvider.generateTokenDto(authentication, memberLoginRequestDto.getAutoLogin());

        String existsRefreshToken = redisUtil.getDataWithKey(authentication.getName());
        if (existsRefreshToken == null){
            if(memberTokenDto.getAutoLogin()){
                redisUtil.setDataWithExpire(authentication.getName(), memberTokenDto.getRefreshToken(), REFRESH_TOKEN_AUTOLOGIN_TTL);
            }

            else{
                redisUtil.setDataWithExpire(authentication.getName(), memberTokenDto.getRefreshToken(), REFRESH_TOKEN_TTL);
            }
        }

        else{
            if(memberTokenDto.getAutoLogin()){
                redisUtil.setDataWithExpire(authentication.getName(), existsRefreshToken, REFRESH_TOKEN_AUTOLOGIN_TTL);
            }

            else{
                redisUtil.setDataWithExpire(authentication.getName(), existsRefreshToken, REFRESH_TOKEN_TTL);
            }
            memberTokenDto.setRefreshToken(existsRefreshToken);
        }

        memberTokenDto.setProvider("GENERAL");
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
        String refreshToken = redisUtil.getDataWithKey(authentication.getName());

        if(refreshToken == null){
            throw new RuntimeException("로그아웃 된 사용자입니다.");
        }

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.equals(memberTokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        MemberTokenDto memberTokenDto = tokenProvider.generateTokenDto(authentication, memberTokenRequestDto.getAutoLogin());
        if(memberTokenDto.getAutoLogin()){
            redisUtil.setDataWithExpire(authentication.getName(), memberTokenDto.getRefreshToken(), REFRESH_TOKEN_AUTOLOGIN_TTL);
        }

        else{
            redisUtil.setDataWithExpire(authentication.getName(), memberTokenDto.getRefreshToken(), REFRESH_TOKEN_TTL);
        }
        return memberTokenDto;
    }


    public boolean duplicateCheckByLoginId(LoginIdDuplicateDto loginIdDuplicateDto){
        if(memberRepository.findByLoginId(loginIdDuplicateDto.getLoginId()).isPresent()){
            return true;
        }
        else{
            return false;
        }
    }


    public boolean duplicateCheckByEmail(EmailDuplicateDto emailDuplicateDto){
        if(memberRepository.findByEmail(emailDuplicateDto.getEmail()).isPresent()){
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public ResponseDto changePassword(PasswordRequestDto passwordRequestDto) {
        Member member = memberRepository.findByEmail(passwordRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if (member.getMemberProvider() == MemberProvider.KAKAO){
            throw new RuntimeException("카카오 로그인으로 가입한 회원입니다. 카카오 로그인을 이용하세요");
        }

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

        if (findMember.getMemberProvider() == MemberProvider.KAKAO){
            throw new RuntimeException("카카오 로그인으로 가입한 회원입니다. 카카오 로그인을 이용하세요");
        }

        mailService.authLoginId(loginIdRequestDto.getEmail(), loginIdRequestDto.getName(), findMember.getLoginId());
        return new ResponseDto(loginIdRequestDto.getName() + "님 이메일로 아이디가 전송되었습니다.");
    }
}
