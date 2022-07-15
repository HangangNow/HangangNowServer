package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.member.dto.MemberResponseDto;
import com.hangangnow.mainserver.domain.member.dto.PasswordRequestDto;
import com.hangangnow.mainserver.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo(){
        return new ResponseEntity<>(memberService.getLoginMemberInfo(), HttpStatus.OK);
    }


    @PostMapping("/api/v1/members/logout")
    public ResponseEntity<ResponseDto> logout(){
        return new ResponseEntity<>(memberService.logout(), HttpStatus.OK);
    }


    @PutMapping("/api/v1/members/password")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody PasswordRequestDto passwordRequestDto){
        return new ResponseEntity<>(memberService.changePassword(passwordRequestDto), HttpStatus.OK);
    }


    @DeleteMapping("api/v1/members")
    public ResponseEntity<ResponseDto> deleteMember(){
        return new ResponseEntity<>(memberService.deleteMember(), HttpStatus.OK);
    }


    @GetMapping("/api/v1/members/{email}")
    public ResponseEntity<MemberResponseDto> getMemberInfo(@PathVariable String email){
        return new ResponseEntity<>(memberService.getMemberInfoByEmail(email), HttpStatus.OK);
    }
}
