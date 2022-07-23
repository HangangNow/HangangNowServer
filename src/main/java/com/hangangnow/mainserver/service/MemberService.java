package com.hangangnow.mainserver.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.member.MemberProvider;
import com.hangangnow.mainserver.domain.member.dto.*;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@PropertySource("classpath:/application-secret.properties")
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${hangangnow.api.admin.key}")
    private String adminKey;


    @Transactional
    public ResponseDto deleteMember(){
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));

        if(member.getMemberProvider() == MemberProvider.KAKAO){
            disconnectKakaoAccount(member);
        }

        refreshTokenRepository.delete(memberId);
        memberRepository.delete(memberId);
        return new ResponseDto("회원탈퇴가 정상적으로 처리되었습니다.");
    }


    public void disconnectKakaoAccount(Member member){
        String reqURL = "https://kauth.kakao.com/v1/user/unlink";


        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("Authorization=KakaoAK " + adminKey);
            sb.append("&target_id_type=user_id");
            sb.append("&target_id=" + member.getKakaoId());
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException("카카오 서버 인증에 실패하였습니다.");
        }
    }


    @Transactional
    public ResponseDto logout() {
        refreshTokenRepository.delete(SecurityUtil.getCurrentMemberId());
        return new ResponseDto("로그아웃이 정상적으로 처리되었습니다.");
    }


    public MemberResponseDto getMemberInfoByEmail(String email){
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다."));
    }


    public MemberResponseDto getLoginMemberInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저 정보가 없습니다"));
    }


    @Transactional
    public ResponseDto changePassword(PasswordRequestDto passwordRequestDto) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다."));

        // System.out.println("passwordRequestDto = " + passwordRequestDto.getPassword1());


        if (!passwordRequestDto.getPassword1().equals(passwordRequestDto.getPassword2())){
            throw new IllegalArgumentException("비밀번호가 서로 일치하지 않습니다");
        }

        if (passwordEncoder.matches(passwordRequestDto.getPassword1(), findMember.getPassword())){
            throw new IllegalArgumentException("기존 비밀번호와 일치합니다.");
        }

        // 로그인 되어있는 refresh 토큰 삭제 -> 로그아웃
        // 클라이언트와 협의 해야함.
        refreshTokenRepository.delete(findMember.getId());

        findMember.updatePassword(passwordEncoder.encode(passwordRequestDto.getPassword1()));

        return new ResponseDto("비밀번호가 정상적으로 변경되었습니다.");

    }
}
