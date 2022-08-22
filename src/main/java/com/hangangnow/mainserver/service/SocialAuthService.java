package com.hangangnow.mainserver.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hangangnow.mainserver.config.security.KakaoAuthenticationProvider;
import com.hangangnow.mainserver.config.redis.RedisUtil;
import com.hangangnow.mainserver.config.jwt.TokenProvider;
import com.hangangnow.mainserver.domain.member.Authority;
import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.member.MemberProvider;
import com.hangangnow.mainserver.domain.member.dto.Gender;
import com.hangangnow.mainserver.domain.member.dto.KakaoMemberDto;
import com.hangangnow.mainserver.domain.member.dto.MemberKakaoTokenDto;
import com.hangangnow.mainserver.domain.member.dto.MemberTokenDto;
import com.hangangnow.mainserver.repository.MemberRepository;
import com.hangangnow.mainserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@PropertySource("classpath:/application-secret.properties")
public class SocialAuthService {

    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final KakaoAuthenticationProvider kakaoAuthenticationProvider;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final long REFRESH_TOKEN_TTL = 60 * 60 * 24 * 7;  // 7일(s)
    private static final long REFRESH_TOKEN_AUTOLOGIN_TTL = 60 * 60 * 24 * 90;  // 90일(s)


    @Value("${hangangnow.api.restapi.key}")
    private String restApiKey;


    public MemberKakaoTokenDto loginByKakaoToken(String accessToken, Boolean autoLogin){

        KakaoMemberDto memberKakaoDto = new KakaoMemberDto();

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token 이용해 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

            //결과 코드가 200 -> 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON 타입 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON 파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            memberKakaoDto = getKakaoUserAttribute(element);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            return login(memberKakaoDto, autoLogin);
        }
    }


    private KakaoMemberDto getKakaoUserAttribute(JsonElement element) {

        long kakaoId = element.getAsJsonObject().get("id").getAsLong();
        String name = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
        String email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
        boolean hasGender = element.getAsJsonObject().get("kakao_account" ).getAsJsonObject().get("has_gender").getAsBoolean();

        Gender gender = null;
        if (hasGender){
            System.out.println("gender = " + element.getAsJsonObject().get("kakao_account" ).getAsJsonObject().get("gender").getAsString().toUpperCase());
            if (element.getAsJsonObject().get("kakao_account" ).getAsJsonObject().get("gender").getAsString().equalsIgnoreCase("MALE")){
                gender = Gender.MALE;
            }
            else gender = Gender.FEMALE;
        }

        int index = email.indexOf("@");
        String loginId = email.substring(0, index) + "_kakao";

        return new KakaoMemberDto(kakaoId, loginId, email, name, gender);
    }


    public MemberKakaoTokenDto login(KakaoMemberDto memberKakaoDto, Boolean autoLogin){
        Member findMemberByKakao = memberRepository.findByEmail(memberKakaoDto.getEmail())
                .orElse(null);

        if(findMemberByKakao == null){
            log.info(memberKakaoDto.getEmail() + "님은 한강나우 회원이 아닙니다. 회원가입을 진행합니다");
            findMemberByKakao = Member.builder()
                    .loginId(memberKakaoDto.getLoginId())
                    .kakaoId(memberKakaoDto.getKakaoId())
                    .email(memberKakaoDto.getEmail())
                    .password(UUID.randomUUID().toString())
                    .name(memberKakaoDto.getName())
                    .gender(memberKakaoDto.getGender())
                    .authority(Authority.ROLE_USER)
                    .memberProvider(MemberProvider.KAKAO)
                    .alarm_agree(false)
                    .marketing_agree(false)
                    .build();
            memberRepository.save(findMemberByKakao);
        }

        UsernamePasswordAuthenticationToken authenticationToken = memberKakaoDto.toAuthentication();

        Authentication authentication = kakaoAuthenticationProvider.authenticate(authenticationToken);

        MemberTokenDto memberTokenDto = tokenProvider.generateTokenDto(authentication, autoLogin);

        String existsRefreshToken = redisUtil.getDataWithKey(authentication.getName());
        if (existsRefreshToken == null){
            // Redis <String, String> -> <memberId, refreshToken> 으로 저장
            if(memberTokenDto.getAutoLogin()){
                redisUtil.setDataWithExpire(authentication.getName(), memberTokenDto.getRefreshToken(), REFRESH_TOKEN_AUTOLOGIN_TTL);
            }

            else{
                redisUtil.setDataWithExpire(authentication.getName(), memberTokenDto.getRefreshToken(), REFRESH_TOKEN_TTL);
            }
        }

        else{
            if(memberTokenDto.getAutoLogin()){
                redisUtil.setDataWithExpire(authentication.getName(), existsRefreshToken, REFRESH_TOKEN_AUTOLOGIN_TTL);
            }

            else{
                redisUtil.setDataWithExpire(authentication.getName(), existsRefreshToken, REFRESH_TOKEN_TTL);
            }

            memberTokenDto.setRefreshToken(existsRefreshToken);
        }


        return MemberKakaoTokenDto.builder()
                .grantType(memberTokenDto.getGrantType())
                .accessToken(memberTokenDto.getAccessToken())
                .refreshToken(memberTokenDto.getRefreshToken())
                .accessTokenExpiresIn(memberTokenDto.getAccessTokenExpiresIn())
                .email(memberKakaoDto.getEmail())
                .name(memberKakaoDto.getName())
                .provider("KAKAO")
                .gender(memberKakaoDto.getGender())
                .build();
    }


    // 카카오 서버에서 벡엔드로 코드 보내주는 테스트 용도
    public String getKakaoAccessToken (String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + restApiKey);
            sb.append("&redirect_uri=http://localhost:8080/api/v1/auth/kakao");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode: " + responseCode);
//            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

//            System.out.println("access_token : " + access_Token);
//            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException("카카오 서버 인증에 실패하였습니다.");
        }

        return access_Token;
    }

}
