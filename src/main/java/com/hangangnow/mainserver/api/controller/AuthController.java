package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.member.dto.*;
import com.hangangnow.mainserver.service.AuthService;
import com.hangangnow.mainserver.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;


    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberSignupRequestDto memberSignupRequestDto) {
        return new ResponseEntity<>(authService.signup(memberSignupRequestDto), HttpStatus.OK);
    }


    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<MemberTokenDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        return new ResponseEntity<>(authService.login(memberLoginRequestDto), HttpStatus.OK);
    }


    @PutMapping("/api/v1/auth/password")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody PasswordRequestDto passwordRequestDto){
        return new ResponseEntity<>(authService.changePassword(passwordRequestDto), HttpStatus.OK);
    }


    @GetMapping("/api/v1/auth/loginId")
    public ResponseEntity<ResponseDto> findLoginId(@RequestBody MemberDuplicateDto memberDuplicateDto){
        return new ResponseEntity<>(authService.findLoginIdByEmail(memberDuplicateDto.getEmail()), HttpStatus.OK);
    }


    @GetMapping("/api/v1/auth/dup/loginId")
    public ResponseEntity<Boolean> loginIdDuplicateCheck(@RequestBody MemberDuplicateDto memberDuplicateDto){
        return new ResponseEntity<>(authService.duplicateCheckByLoginId(memberDuplicateDto), HttpStatus.OK);
    }


    @GetMapping("/api/v1/auth/dup/email")
    public ResponseEntity<Boolean> emailDuplicateCheck(@RequestBody MemberDuplicateDto memberDuplicateDto){
        return new ResponseEntity<>(authService.duplicateCheckByEmail(memberDuplicateDto.getEmail()), HttpStatus.OK);
    }


    @PostMapping("/api/v1/auth/reissue")
    public ResponseEntity<MemberTokenDto> reissue(@RequestBody MemberTokenRequestDto memberTokenRequestDto){
        return new ResponseEntity<>(authService.reissue(memberTokenRequestDto), HttpStatus.CREATED);
    }


    @PostMapping("/api/v1/auth/emailAuth")
    public ResponseEntity<EmailAuthDto> emailAuthenticate(@RequestBody EmailAuthDto emailAuthDto){
        if(!authService.duplicateCheckByEmail(emailAuthDto.getEmail())){
            throw new IllegalArgumentException("존재하지 않는 이메일 입니다.");
        }
        return new ResponseEntity<>(mailService.authEmail(emailAuthDto), HttpStatus.CREATED);
    }



}
