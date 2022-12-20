package com.hangangnow.mainserver.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hangangnow.mainserver.config.security.SecurityConfig;

import com.hangangnow.mainserver.member.dto.EmailDuplicateDto;
import com.hangangnow.mainserver.member.dto.LoginIdDuplicateDto;
import com.hangangnow.mainserver.member.dto.MemberSignupRequestDto;
import com.hangangnow.mainserver.member.service.AuthService;
import com.hangangnow.mainserver.member.service.MailService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Stream;

import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @MockBean
    MailService mailService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() throws Exception {
        MemberSignupRequestDto memberSignupRequestDto = new MemberSignupRequestDto("test1234", "test1234", "test@gmailcom", "Kim",
                "1999-09-14", "MALE");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(memberSignupRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 회원가입_성공_테스트() throws Exception {
        MemberSignupRequestDto memberSignupRequestDto = new MemberSignupRequestDto("test0914", "test0914", "test0914@gmailcom", "Kim",
                "1998-09-14", "MALE");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(memberSignupRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 아이디가_조건에_맞지_않으면_회원가입은_실패한다() throws Exception {
        MemberSignupRequestDto memberSignupRequestDto = new MemberSignupRequestDto("test", "test0914", "test0914@gmailcom", "Kim",
                "1998-09-14", "MALE");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(memberSignupRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    void 비밀번호가_조건에_맞지_않으면_회원가입은_실패한다() throws Exception {
        MemberSignupRequestDto memberSignupRequestDto = new MemberSignupRequestDto("test1234", "test", "test0914@gmailcom", "Kim",
                "1998-09-14", "MALE");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(memberSignupRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource(value = "generateEmailDupReturnValue")
    void 이메일_중복확인_테스트(String expected, boolean returned) throws Exception {
        // given
        EmailDuplicateDto emailDuplicateDto = new EmailDuplicateDto();
        emailDuplicateDto.setEmail("kosa0914@naver.com");

        // when
        doReturn(returned)
                .when(authService).checkDuplicateEmail(emailDuplicateDto);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/dup/email")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(emailDuplicateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String actual = result.getResponse().getContentAsString();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> generateEmailDupReturnValue() {
        return Stream.of(
                Arguments.of("true", true),
                Arguments.of("false", false)
        );
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource(value = "generateLoginIdDupReturnValue")
    void 로그인_아이디_중복확인_테스트(String expected, boolean returned) throws Exception {
        // given
        LoginIdDuplicateDto loginIdDuplicateDto = new LoginIdDuplicateDto();
        loginIdDuplicateDto.setLoginId("test1234");

        // when
        doReturn(returned)
                .when(authService).checkDuplicateLoginId(loginIdDuplicateDto);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/dup/loginId")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(loginIdDuplicateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String actual = result.getResponse().getContentAsString();
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> generateLoginIdDupReturnValue() {
        return Stream.of(
                Arguments.of("true", true),
                Arguments.of("false", false)
        );
    }
}