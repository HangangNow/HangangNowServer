package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.member.dto.MemberResponseDto;
import com.hangangnow.mainserver.domain.member.dto.PasswordRequestDto;
import com.hangangnow.mainserver.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "현재 멤버 정보 조회", description = "현재 로그인 한 멤버 정보 조회 요청 URL." +
            "\n### 요청변수: X")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),

    })
    @GetMapping("/api/v1/members")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo(){
        return new ResponseEntity<>(memberService.getLoginMemberInfo(), HttpStatus.OK);
    }


    @Operation(summary = "로그아웃", description = "로그인 멤버 로그아웃 요청 URL" +
            "\n### 요청변수: X")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    @PostMapping("/api/v1/members/logout")
    public ResponseEntity<ResponseDto> logout(){
        return new ResponseEntity<>(memberService.logout(), HttpStatus.OK);
    }


    @Operation(summary = "로그인 -> 비밀번호 변경", description = "현재 로그인 한 멤버 비밀번호 변경 요청 URL" +
            "\n### 요청변수: email, password1, password2" +
            "\n### 비밀번호 정상 변경 시 로그아웃 처리 -> 다시 로그인 하도록")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PutMapping("/api/v1/members/password")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody @Valid PasswordRequestDto passwordRequestDto){
        return new ResponseEntity<>(memberService.changePassword(passwordRequestDto), HttpStatus.OK);
    }


    @Operation(summary = "회원탈퇴", description = "회원탈퇴 요청 URL. " +
            "\n### 요청변수 X" +
            "\n### 카카오 로그인 회원은 자동으로 카카오 연동 끊어짐")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @DeleteMapping("api/v1/members")
    public ResponseEntity<ResponseDto> deleteMember(){
        return new ResponseEntity<>(memberService.deleteMember(), HttpStatus.OK);
    }


    @Operation(summary = "이메일로 현재 멤버 정보 조회(테스트 용)", description = "멤버 정보 조회 요청 URL.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @GetMapping("/api/v1/members/{email}")
    public ResponseEntity<MemberResponseDto> getMemberInfoByEmail(@PathVariable String email){
        return new ResponseEntity<>(memberService.getMemberInfoByEmail(email), HttpStatus.OK);
    }


//    @PostMapping("/api/v1/members/profiles")
//    public ResponseEntity<String> changeMemberProfile(
//            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest) throws Exception{
//
//        return new ResponseEntity<>(memberService.changeProfile(imageRequest), HttpStatus.OK);
//    }
}
