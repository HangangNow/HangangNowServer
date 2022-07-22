package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.member.dto.MemberTokenDto;
import com.hangangnow.mainserver.service.SocialAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SocialAuthController {

    private final SocialAuthService socialAuthService;

    @GetMapping("/api/v1/auth/kakao")
    public ResponseEntity<MemberTokenDto> kakaoCode(@RequestParam String code){
        System.out.println("code = " + code);
        String accessToken = socialAuthService.getKakaoAccessToken(code);
        return new ResponseEntity<>(socialAuthService.loginByKakaoToken(accessToken), HttpStatus.OK);
    }




    @PostMapping("/api/v1/auth/kakao")
    public ResponseEntity<MemberTokenDto> kakaoLogin(@RequestParam String accessToken){
        return new ResponseEntity<>(socialAuthService.loginByKakaoToken(accessToken), HttpStatus.OK);
    }
}
