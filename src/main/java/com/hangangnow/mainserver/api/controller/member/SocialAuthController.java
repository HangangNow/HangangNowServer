package com.hangangnow.mainserver.api.controller.member;

import com.hangangnow.mainserver.domain.member.dto.MemberKakaoTokenDto;
import com.hangangnow.mainserver.service.SocialAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public ResponseEntity<MemberKakaoTokenDto> kakaoCode(@RequestParam String code){
        System.out.println("code = " + code);
        String accessToken = socialAuthService.getKakaoAccessToken(code);
        return new ResponseEntity<>(socialAuthService.loginByKakaoToken(accessToken, false), HttpStatus.OK);
    }




    @Operation(summary = "카카오 로그인", description = "카카오 로그인 URL. " +
            "\n### RequestParam: accessToken, autoLogin" +
            "\n### /api/v1/auth/kakao?accessToken=xxxxxxxxx&autoLogin=true or false")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "405", description = "METHOD NOT Allowed"),

    })
    @PostMapping("/api/v1/auth/kakao")
    public ResponseEntity<MemberKakaoTokenDto> kakaoLogin(@RequestParam String accessToken, @RequestParam(defaultValue = "false") Boolean autoLogin){
        log.info("Kakao Login API Called");
        return new ResponseEntity<>(socialAuthService.loginByKakaoToken(accessToken, autoLogin), HttpStatus.OK);
    }
}
