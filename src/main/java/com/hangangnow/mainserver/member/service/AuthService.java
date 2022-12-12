package com.hangangnow.mainserver.member.service;

import com.hangangnow.mainserver.config.security.MemberAuthenticationProvider;

import com.hangangnow.mainserver.member.dto.*;
import com.hangangnow.mainserver.util.redis.RedisUtil;
import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.member.entity.MemberProvider;
import com.hangangnow.mainserver.config.jwt.TokenProvider;
import com.hangangnow.mainserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public MemberResponseDto signup(MemberSignupRequestDto memberSignupRequestDto) {
        if (isAlreadySignup(memberSignupRequestDto.getLoginId())) {
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다.");
        }

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberResponseDto(memberRepository.save(memberSignupRequestDto.toMember(passwordEncoder)));

        return memberResponseDto;
    }

    private boolean isAlreadySignup(String loginId) {
        return memberRepository.findByLoginId(loginId).isPresent();
    }


    @Transactional
    public MemberTokenDto login(MemberLoginRequestDto memberLoginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = memberLoginRequestDto.toAuthentication();
        Authentication authentication = memberAuthenticationProvider.authenticate(authenticationToken);

        MemberTokenDto memberTokenDto = tokenProvider.generateTokenDto(authentication, memberLoginRequestDto.getAutoLogin());
        String existsRefreshToken = redisUtil.getDataWithKey(authentication.getName());

        updateRefreshToken(authentication, memberTokenDto, existsRefreshToken);

        memberTokenDto.setProvider("GENERAL");
        return memberTokenDto;
    }

    private void updateRefreshToken(Authentication authentication, MemberTokenDto memberTokenDto, String refreshToken) {
        if (isRefreshTokenExist(refreshToken)) {
            setMemberToken(authentication, refreshToken, memberTokenDto.getAutoLogin());
            memberTokenDto.setRefreshToken(refreshToken);
        } else {
            setMemberToken(authentication, memberTokenDto.getRefreshToken(), memberTokenDto.getAutoLogin());
        }
    }

    private boolean isRefreshTokenExist(String existsRefreshToken) {
        return existsRefreshToken != null;
    }


    @Transactional
    public MemberTokenDto reissueToken(MemberTokenRequestDto memberTokenRequestDto) {
        validateRefreshToken(memberTokenRequestDto);

        Authentication authentication = tokenProvider.getAuthentication(memberTokenRequestDto.getAccessToken());
        String refreshToken = redisUtil.getDataWithKey(authentication.getName());

        validateTokenMember(memberTokenRequestDto, refreshToken);

        MemberTokenDto memberTokenDto = tokenProvider.generateTokenDto(authentication, memberTokenRequestDto.getAutoLogin());
        setMemberToken(authentication, memberTokenDto.getRefreshToken(), memberTokenDto.getAutoLogin());
        return memberTokenDto;
    }

    private void setMemberToken(Authentication authentication, String refreshToken, boolean isAutoLogin) {
        if (isAutoLogin) {
            redisUtil.setDataWithExpire(authentication.getName(), refreshToken, REFRESH_TOKEN_AUTOLOGIN_TTL);
        } else {
            redisUtil.setDataWithExpire(authentication.getName(), refreshToken, REFRESH_TOKEN_TTL);
        }
    }

    private void validateTokenMember(MemberTokenRequestDto memberTokenRequestDto, String refreshToken) {
        if (isRefreshTokenExist(refreshToken)) {
            throw new RuntimeException("로그아웃 된 사용자입니다.");
        }

        if (!refreshToken.equals(memberTokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
    }

    private void validateRefreshToken(MemberTokenRequestDto memberTokenRequestDto) {
        if (!tokenProvider.validateToken(memberTokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token 이 유효하지 않습니다.");
        }
    }

    public boolean checkDuplicateLoginId(LoginIdDuplicateDto loginIdDuplicateDto) {
        return memberRepository.findByLoginId(loginIdDuplicateDto.getLoginId()).isPresent();
    }

    public boolean checkDuplicateEmail(EmailDuplicateDto emailDuplicateDto) {
        return memberRepository.findByEmail(emailDuplicateDto.getEmail()).isPresent();
    }

    @Transactional
    public ResponseDto changePassword(PasswordRequestDto passwordRequestDto) {
        Member member = getMemberOrException(passwordRequestDto);

        if (member.getMemberProvider() == MemberProvider.KAKAO) {
            throw new RuntimeException("카카오 로그인으로 가입한 회원입니다. 카카오 로그인을 이용하세요");
        }

        String password1 = passwordRequestDto.getPassword1();
        String password2 = passwordRequestDto.getPassword2();

        validatePasswords(member, password1, password2);

        member.updatePassword(passwordEncoder.encode(password1));

        return new ResponseDto("비밀번호가 정상적으로 변경되었습니다.");
    }

    private void validatePasswords(Member member, String password1, String password2) {
        if (!password1.equals(password2)) {
            throw new IllegalArgumentException("비밀번호가 서로 일치하지 않습니다");
        }

        if (passwordEncoder.matches(password1, member.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 일치합니다.");
        }
    }

    private Member getMemberOrException(PasswordRequestDto passwordRequestDto) {
        return memberRepository.findByEmail(passwordRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));
    }

    public ResponseDto findLoginIdByEmail(LoginIdRequestDto loginIdRequestDto) {
        Member findMember = memberRepository.findByEmail(loginIdRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 아이디가 존재하지 않습니다"));

        if (findMember.getMemberProvider() == MemberProvider.KAKAO) {
            throw new RuntimeException("카카오 로그인으로 가입한 회원입니다. 카카오 로그인을 이용하세요");
        }

        mailService.authLoginId(loginIdRequestDto.getEmail(), loginIdRequestDto.getName(), findMember.getLoginId());
        return new ResponseDto(loginIdRequestDto.getName() + "님 이메일로 아이디가 전송되었습니다.");
    }
}
