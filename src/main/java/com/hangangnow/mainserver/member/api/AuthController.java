package com.hangangnow.mainserver.member.api;


import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.member.dto.*;
import com.hangangnow.mainserver.member.service.AuthService;
import com.hangangnow.mainserver.member.service.MailService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

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
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<MemberTokenDto> login(@RequestBody @Valid MemberLoginRequestDto memberLoginRequestDto) {
        return new ResponseEntity<>(authService.login(memberLoginRequestDto), HttpStatus.OK);
    }


    @Operation(summary = "비밀번호 찾기 -> 비밀번호 변경", description = "비밀번호 변경 요청 URL" +
            "\n### 요청변수: 모든 변수")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PutMapping("/api/v1/auth/password")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody @Valid PasswordRequestDto passwordRequestDto) {
        return new ResponseEntity<>(authService.changePassword(passwordRequestDto), HttpStatus.OK);
    }


    @Operation(summary = "아이디 찾기", description = "아이디 찾기 요청 URL" +
            "\n### 요청변수: name, email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/auth/loginId")
    public ResponseEntity<ResponseDto> findLoginId(@RequestBody @Valid LoginIdRequestDto loginIdRequestDto) {
        return new ResponseEntity<>(authService.findLoginIdByEmail(loginIdRequestDto), HttpStatus.OK);
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
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/auth/dup/loginId")
    public ResponseEntity<Boolean> loginIdDuplicateCheck(@RequestBody @Valid LoginIdDuplicateDto memberDuplicateDto) {
        return new ResponseEntity<>(authService.checkDuplicateLoginId(memberDuplicateDto), HttpStatus.OK);
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
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/auth/dup/email")
    public ResponseEntity<Boolean> emailDuplicateCheck(@RequestBody @Valid EmailDuplicateDto emailDuplicateDto) {
        return new ResponseEntity<>(authService.checkDuplicateEmail(emailDuplicateDto), HttpStatus.OK);
    }


    @Operation(summary = "토큰 재발급", description = "Access Token 만료 시 재발급 요청 URL" +
            "\n### 요청변수: accessToken, refreshToken" +
            "\n### **토큰 재발급 시 Refresh Token 값은 변경됩니다.**" +
            "\n### provider는 항상 null 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/auth/reissue")
    public ResponseEntity<MemberTokenDto> reissue(@RequestBody MemberTokenRequestDto memberTokenRequestDto) {
        return new ResponseEntity<>(authService.reissueToken(memberTokenRequestDto), HttpStatus.OK);
    }


    @Operation(summary = "이메일 인증코드 발송", description = "이메일 인증코드 발송 요청 URL." +
            "\n### 요청변수: email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/auth/emailAuth")
    public ResponseEntity<EmailAuthDto> sendEmailAuthenticate(@RequestBody @Valid EmailAuthDto emailAuthDto) {
        return new ResponseEntity<>(mailService.authEmail(emailAuthDto), HttpStatus.OK);
    }


    @Operation(summary = "이메일 인증코드 확인", description = "이메일 인증코드 확인 요청 URL." +
            "\n### 요청변수: email, code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/auth/emailAuth/code")
    public ResponseEntity<Boolean> checkEmailAuthenticate(@RequestBody @Valid EmailAuthDto emailAuthDto) {
        return new ResponseEntity<>(mailService.checkEmailCode(emailAuthDto), HttpStatus.OK);
    }
}
