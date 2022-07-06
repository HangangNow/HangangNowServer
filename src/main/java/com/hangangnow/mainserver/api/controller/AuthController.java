package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.member.dto.*;
import com.hangangnow.mainserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    // 카카오 로그인 code 받기
    @GetMapping("/api/v1/auth/kakao")
    public String getKakaoCode(@RequestParam(name = "code") String code){
        log.info("kakao code = " + code);
        return "ok";
    }


    @PostMapping("/api/v1/auth/loginId")
    public ResponseEntity<Boolean> loginIdCheck(@RequestBody MemberDuplicateDto memberDuplicateDto){
        log.info("loginId : " + memberDuplicateDto.getLoginId());
        return ResponseEntity.ok(authService.duplicateCheckByLoginId(memberDuplicateDto));
    }


    @PostMapping("/api/v1/auth/email")
    public ResponseEntity<Boolean> emailCheck(@RequestBody MemberDuplicateDto memberDuplicateDto){
        log.info("loginId : " + memberDuplicateDto.getEmail());
        return ResponseEntity.ok(authService.duplicateCheckByEmail(memberDuplicateDto));
    }


    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<MemberSignupResponseDto> signup(@RequestBody MemberSignupRequestDto memberSignupRequestDto) {
        return ResponseEntity.ok(authService.signup(memberSignupRequestDto));
    }


    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<MemberLoginTokenDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        return ResponseEntity.ok(authService.login(memberLoginRequestDto));
    }




}
