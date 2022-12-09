package com.hangangnow.mainserver.service;

import com.hangangnow.mainserver.config.redis.RedisUtil;
import com.hangangnow.mainserver.config.jwt.SecurityUtil;
import com.hangangnow.mainserver.config.s3.S3Uploader;
import com.hangangnow.mainserver.domain.common.ResponseDto;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.member.MemberProvider;
import com.hangangnow.mainserver.domain.member.dto.*;
import com.hangangnow.mainserver.domain.mypage.WithdrawalReason;
import com.hangangnow.mainserver.domain.mypage.dto.WithdrawDto;
import com.hangangnow.mainserver.domain.photo.MemberPhoto;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.WithdrawalReasonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@PropertySource("classpath:/application-secret.properties")
public class MemberService {

    private final MemberRepository memberRepository;
    private final WithdrawalReasonRepository withdrawalReasonRepository;

    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    @Value("${hangangnow.api.admin.key}")
    private String adminKey;


    @Transactional
    public ResponseDto deleteMember(){
        UUID memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다"));

        if(member.getMemberProvider() == MemberProvider.KAKAO){
            disconnectKakaoAccount(member);
        }

        redisUtil.deleteData(memberId.toString());
        memberRepository.delete(member);
        return new ResponseDto("회원탈퇴가 정상적으로 처리되었습니다.");
    }

    @Transactional
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
            log.info("responseCode : " + responseCode + " 카카오 회원탈퇴 완료");

        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException("카카오 서버 인증에 실패하였습니다.");
        }
    }


    @Transactional
    public ResponseDto logout() {
        redisUtil.deleteData(SecurityUtil.getCurrentMemberId().toString());
        return new ResponseDto("로그아웃이 정상적으로 처리되었습니다.");
    }


    public MemberResponseDto getMemberInfoByEmail(String email){
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다."));

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberResponseDto(findMember);

        return memberResponseDto;
    }


    public MemberResponseDto getLoginMemberInfo() {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저 정보가 없습니다"));

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberResponseDto(findMember);

        return memberResponseDto;
    }


    @Transactional
    public ResponseDto changePassword(PasswordRequestDto passwordRequestDto) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 없습니다."));


        if (!passwordRequestDto.getPassword1().equals(passwordRequestDto.getPassword2())){
            throw new IllegalArgumentException("비밀번호가 서로 일치하지 않습니다");
        }

        if (passwordEncoder.matches(passwordRequestDto.getPassword1(), findMember.getPassword())){
            throw new IllegalArgumentException("기존 비밀번호와 일치합니다.");
        }

        // 로그인 되어있는 refresh 토큰 삭제 -> 로그아웃
        redisUtil.deleteData(findMember.getId().toString());

        findMember.updatePassword(passwordEncoder.encode(passwordRequestDto.getPassword1()));

        return new ResponseDto("비밀번호가 정상적으로 변경되었습니다.");

    }


    @Transactional
    public ResponseDto changeMemberMbti(String mbti) {
        String[] mbtiList = new String[]{"INFLUENCER", "EXCITED", "ARTIST", "SOCIAL_DISTANCING",
                "ACTIVIST", "PLANNER", "EXPLORER", "STARGAZER"};

        if (!Arrays.asList(mbtiList).contains(mbti)){
            throw new IllegalArgumentException("존재하지 않는 MBTI 입니다");
        }
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        findMember.updateMbti(mbti);
        return new ResponseDto("mbti가 정상적으로 설정되었습니다.");
    }

    @Transactional
    public ResponseDto deleteMemberMbti() {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        findMember.updateMbti(null);
        return new ResponseDto("mbti가 정상적으로 삭제되었습니다.");
    }


    @Transactional
    public ResponseDto changePhoto(MultipartFile multipartFile) throws IOException {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("멤버 조회를 실패했습니다."));

        // 프로필을 없애는 경우
        if (multipartFile == null){
            throw new IllegalArgumentException("이미지 파일이 존재하지 않습니다.");
        }

        MemberPhoto findMemberPhoto = findMember.getPhoto();

        if(findMemberPhoto == null){
            MemberPhoto memberPhoto = new MemberPhoto(s3Uploader.upload(multipartFile, "profile"));
            findMember.updatePhoto(memberPhoto);
        }

        else{
            s3Uploader.delete(findMemberPhoto.getS3Key());
            MemberPhoto memberPhoto = new MemberPhoto(s3Uploader.upload(multipartFile, "profile"));
            findMember.updatePhoto(memberPhoto);
        }


        return new ResponseDto(findMember.getPhoto().getUrl());
    }


    @Transactional
    public ResponseDto deletePhoto() throws IOException{
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        MemberPhoto findMemberPhoto = findMember.getPhoto();

        if (findMemberPhoto == null){
            throw new RuntimeException("회원 프로필 사진이 없습니다.");
        }

        s3Uploader.delete(findMemberPhoto.getS3Key());
        findMember.updatePhoto(null);

        return new ResponseDto("프로필이 정상적으로 삭제되었습니다.");
    }

    @Transactional
    public ResponseDto updateMarketingAgree(Boolean flag) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        findMember.updateMarketingAgree(flag);

        return new ResponseDto("마케팅 정보, 메일 수신 동의 처리가 정상적으로 완료되었습니다.");
    }


    @Transactional
    public ResponseDto updateAlarmAgree(Boolean flag) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        findMember.updateAlarmAgree(flag);

        return new ResponseDto("알람설정 처리가 정상적으로 완료되었습니다.");
    }



//    public GenericResponseDto getFlyerScraps(){
//        scrapRepository.findAllScrapsByMemberId(SecurityUtil.getCurrentMemberId())
//                .stream()
//                .map()
//                .collect(Collectors.toList())
//    }


//    public GenericResponseDto getCourseScraps(){
//        scrapRepository.findAllScrapsByMemberId(SecurityUtil.getCurrentMemberId())
//                .stream()
//                .map()
//                .collect(Collectors.toList())
//    }


    @Transactional
    public ResponseDto createWithdrawReason(WithdrawDto withdrawDto){
        WithdrawalReason withdrawalReason = new WithdrawalReason(
                withdrawDto.getDifficulty(), withdrawDto.getFewerEvents(), withdrawDto.getAnotherApplication(),
                withdrawDto.getNoCredibility(), withdrawDto.getNoNeed(), withdrawDto.getEtc(), withdrawDto.getEtcContent()
        );

        withdrawalReasonRepository.save(withdrawalReason);

        return new ResponseDto("탈퇴 사유가 저장되었습니다.");
    }
}
