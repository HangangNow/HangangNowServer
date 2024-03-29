package com.hangangnow.mainserver.member.api;

import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.member.dto.MemberResponseDto;
import com.hangangnow.mainserver.member.dto.PasswordRequestDto;
import com.hangangnow.mainserver.member.dto.WithdrawDto;
import com.hangangnow.mainserver.member.service.MemberService;

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
import java.io.IOException;


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
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
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
    public ResponseEntity<ResponseDto> logout() {
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
    public ResponseEntity<ResponseDto> changePassword(@RequestBody @Valid PasswordRequestDto passwordRequestDto) {
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
    public ResponseEntity<ResponseDto> deleteMember() {
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
    public ResponseEntity<MemberResponseDto> getMemberInfoByEmail(@PathVariable String email) {
        return new ResponseEntity<>(memberService.getMemberInfoByEmail(email), HttpStatus.OK);
    }


    @Operation(summary = "멤버 MBTI 설정, 변경", description = "멤버 MBTI 설정 URL." +
            "\n### ?mbti=xxxxx  " +
            "\n 인플루언서, 흥부자, 예술가, 사회적 거리두기, 활동가, 계획파, 탐구왕, 몽상가" +
            "\n### **INFLUENCER, EXCITED, ARTIST, SOCIAL_DISTANCING, ACTIVIST, PLANNER, EXPLORER, STARGAZER** 중 하나를 requestParam으로 보내면 됩니다." +
            "\n### 일치하지 않는 mbti가 요청으로 넘어오는 경우 exception이 발생합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PutMapping("/api/v1/members/mbti")
    public ResponseEntity<ResponseDto> changeMemberMbti(@RequestParam String mbti) {
        return new ResponseEntity<>(memberService.changeMemberMbti(mbti), HttpStatus.OK);
    }

    @Operation(summary = "멤버 MBTI 삭제", description = "멤버 MBTI 삭제 URL." +
            "\n### 요청변수 X")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @DeleteMapping("/api/v1/members/mbti")
    public ResponseEntity<ResponseDto> deleteMemberMbti() {
        return new ResponseEntity<>(memberService.deleteMemberMbti(), HttpStatus.OK);
    }

    @Operation(summary = "프로필 추가 및 수정", description = "회원 프로필을 추가할 수 있습니다.  " +
            "\nform-data key-value형식으로 데이터를 요청  " +
            "\nkey: multipartData(필수)  " +
            "\n정상적으로 변경된 경우 URL이 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    @PutMapping("/api/v1/members/photos")
    public ResponseEntity<ResponseDto> changeMemberProfile(
            @Valid @RequestPart(value = "multipartData", required = false) MultipartFile imageRequest) throws Exception {
        return new ResponseEntity<>(memberService.changePhoto(imageRequest), HttpStatus.OK);
    }


    @Operation(summary = "프로필 삭제", description = "회원 프로필을 삭제할 수 있습니다.  " +
            "\n요청변수: X  " +
            "\n기존 프로필 사진이 없는데 요청하는 경우 예외가 발생합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    @DeleteMapping("/api/v1/members/photos")
    public ResponseEntity<ResponseDto> deleteMemberProfile() throws IOException {
        return new ResponseEntity<>(memberService.deletePhoto(), HttpStatus.OK);
    }


    @Operation(summary = "마케팅 정보, 메일 수신 동의 변경", description = "회원 마케팅 정보와 메일 수신 동의 정보 변경 요청 URL.  " +
            "\n### ?flag=xxxxx" +
            "\n### **true, false** 중 하나를 requestParam으로 보내면 됩니다." +
            "\n### requestParam 형태가 올바르지 않은 경우, flar가 true, false가 아닌 경우 exception이 발생합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    @PostMapping("/api/v1/members/agree/marketing")
    public ResponseEntity<ResponseDto> updateMarketingAgree(@RequestParam Boolean flag) {
        return new ResponseEntity<>(memberService.updateMarketingAgree(flag), HttpStatus.OK);
    }


    @Operation(summary = "알람 설정 변경", description = "회원 알람 설정 변경 요청 URL.  " +
            "\n### ?flag=xxxxx" +
            "\n### **true, false** 중 하나를 requestParam으로 보내면 됩니다." +
            "\n### requestParam 형태가 올바르지 않은 경우, flar가 true, false가 아닌 경우 exception이 발생합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),
    })
    @PostMapping("/api/v1/members/agree/alarm")
    public ResponseEntity<ResponseDto> updateAlarmAgree(@RequestParam Boolean flag) {
        return new ResponseEntity<>(memberService.updateAlarmAgree(flag), HttpStatus.OK);
    }


    @Operation(summary = "회원탈퇴 사유 등록", description = "회원탈퇴 사유 등록 URL.  " +
            "\n 요청 예시:  " +
            "{\n" +
            "\t\"difficulty\" : \"true\",\n" +
            "\t\"fewerEvents\" : \"false\",\n" +
            "\t\"anotherApplication\" : \"false\",\n" +
            "\t\"noCredibility\" : \"true\",\n" +
            "\t\"noNeed\" : \"false\",\n" +
            "\t\"etc\" : \"true\",\n" +
            "    \"etcContent\" : \"회원탈퇴 사유 테스트입니다. \"\n" +
            "}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/members/withdraw/reasons")
    public ResponseEntity<ResponseDto> registerWithdrawReasons(@RequestBody WithdrawDto withdrawDto) {
        return new ResponseEntity<>(memberService.createWithdrawReason(withdrawDto), HttpStatus.OK);
    }


//    @GetMapping("/api/v1/members/scraps/courses")
//    public ResponseEntity<GenericResponseDto> getCourseScrapInfo(){
//        return new ResponseEntity<>(memberService.getAllScraps(), HttpStatus.OK);
//    }
}
