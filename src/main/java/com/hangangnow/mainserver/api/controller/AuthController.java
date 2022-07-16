package com.hangangnow.mainserver.api.controller;


import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.member.dto.*;
import com.hangangnow.mainserver.service.AuthService;
import com.hangangnow.mainserver.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;

    @Operation(summary = "회원가입", description = "회원가입 요청 URL. " +
            "\n### 요청변수: 모든 변수. " +
            "\n birthday type: **yyyy-mm-dd**  " +
            "\n gender: **MALE** or **FEMALE**"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberSignupRequestDto memberSignupRequestDto) {
        return new ResponseEntity<>(authService.signup(memberSignupRequestDto), HttpStatus.OK);
    }


    @Operation(summary = "로그인", description = "로그인 URL. " +
            "\n### 요청변수: 모든 변수")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<MemberTokenDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        return new ResponseEntity<>(authService.login(memberLoginRequestDto), HttpStatus.OK);
    }


    @Operation(summary = "비밀번호 찾기 -> 비밀번호 변경", description = "비밀번호 변경 요청 URL" +
            "\n### 요청변수: 모든 변수")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @PutMapping("/api/v1/auth/password")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody PasswordRequestDto passwordRequestDto){
        return new ResponseEntity<>(authService.changePassword(passwordRequestDto), HttpStatus.OK);
    }


    @Operation(summary = "아이디 찾기", description = "아이디 찾기 요청 URL" +
            "\n### 요청변수: loginId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @GetMapping("/api/v1/auth/loginId")
    public ResponseEntity<ResponseDto> findLoginId(@RequestBody MemberDuplicateDto memberDuplicateDto){
        return new ResponseEntity<>(authService.findLoginIdByEmail(memberDuplicateDto.getEmail()), HttpStatus.OK);
    }


    @Operation(summary = "로그인 아이디 중복 체크",
            description = "로그인 아이디 중복 체크 요청 URL" +
                    "\n### 요청변수: loginId" +
                    "\n##  응답결과" +
                    "\n- true: 아이디 중복" +
                    "\n- false: 아이디 중복 X"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @GetMapping("/api/v1/auth/dup/loginId")
    public ResponseEntity<Boolean> loginIdDuplicateCheck(@RequestBody MemberDuplicateDto memberDuplicateDto){
        return new ResponseEntity<>(authService.duplicateCheckByLoginId(memberDuplicateDto), HttpStatus.OK);
    }



    @Operation(summary = "이메일 중복 체크",
            description = "이메일 중복 체크 요청 URL. " +
                    "\n### 요청변수: email" +
                    "\n###  응답결과" +
                    "\n- true: 이메일 중복" +
                    "\n- false: 이메일 중복 X"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @GetMapping("/api/v1/auth/dup/email")
    public ResponseEntity<Boolean> emailDuplicateCheck(@RequestBody MemberDuplicateDto memberDuplicateDto){
        return new ResponseEntity<>(authService.duplicateCheckByEmail(memberDuplicateDto.getEmail()), HttpStatus.OK);
    }



    @Operation(summary = "토큰 재발급", description = "Access Token 만료 시 재발급 요청 URL" +
            "\n### 요청변수: accessToken, refreshToken")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @PostMapping("/api/v1/auth/reissue")
    public ResponseEntity<MemberTokenDto> reissue(@RequestBody MemberTokenRequestDto memberTokenRequestDto){
        return new ResponseEntity<>(authService.reissue(memberTokenRequestDto), HttpStatus.CREATED);
    }


    @Operation(summary = "이메일 인증코드 발송", description = "이메일 인증코드 발송 요청 URL." +
            "\n### 요청변수: email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @PostMapping("/api/v1/auth/emailAuth")
    public ResponseEntity<EmailAuthDto> emailAuthenticate(@RequestBody EmailAuthDto emailAuthDto){
        if(!authService.duplicateCheckByEmail(emailAuthDto.getEmail())){
            throw new IllegalArgumentException("존재하지 않는 이메일 입니다.");
        }
        return new ResponseEntity<>(mailService.authEmail(emailAuthDto), HttpStatus.CREATED);
    }
}
