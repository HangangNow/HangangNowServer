package com.hangangnow.mainserver.api.controller;

import com.hangangnow.mainserver.domain.member.dto.MemberResponseDto;
import com.hangangnow.mainserver.domain.member.dto.PasswordRequestDto;
import com.hangangnow.mainserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo(){
        return ResponseEntity.ok(memberService.getLoginMemberInfo());
    }


    @PostMapping("/api/v1/members/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok(memberService.logout());
    }


    @PutMapping("/api/v1/members/password")
    public ResponseEntity<String> changePassword(PasswordRequestDto passwordRequestDto){
        return ResponseEntity.ok(memberService.changePassword(passwordRequestDto));
    }




    @DeleteMapping("api/v1/members")
    public ResponseEntity<String> deleteMember(){
        return ResponseEntity.ok(memberService.deleteMember());
    }


    @GetMapping("/api/v1/members/{email}")
    public ResponseEntity<MemberResponseDto> getMemberInfo(@PathVariable String email){
        return ResponseEntity.ok(memberService.getMemberInfoByEmail(email));
    }




}
