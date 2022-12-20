package com.hangangnow.mainserver.member.api;

import com.hangangnow.mainserver.common.dto.ResponseDto;
import com.hangangnow.mainserver.config.security.SecurityConfig;

import com.hangangnow.mainserver.member.dto.MemberResponseDto;
import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = MemberController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @WithMockUser()
    @Test
    void 현재_로그인된_멤버_정보를_조회한다() throws Exception {
        // given
        MemberResponseDto response = new MemberResponseDto();
        response.setMemberResponseDto(getMember());

        when(memberService.getLoginMemberInfo()).thenReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/members")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("loginId", response.getLoginId()).exists())
                .andExpect(jsonPath("email", response.getEmail()).exists())
                .andExpect(jsonPath("name", response.getName()).exists());
    }

    Member getMember() {
        return Member.builder()
                .loginId("test1234")
                .email("test1234@naver.com")
                .name("test")
                .build();
    }

    @WithMockUser
    @Test
    void 로그아웃_테스트() throws Exception {
        // given
        ResponseDto responseDto = new ResponseDto("logout success.");

        when(memberService.logout()).thenReturn(responseDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/members/logout")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockUser
    @Test
    void 회원탈퇴_테스트() throws Exception {
        // given
        ResponseDto responseDto = new ResponseDto("member delete success");

        when(memberService.deleteMember()).thenReturn(responseDto);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/members")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockUser
    @Test
    void 회원_MBTI_변경_테스트() throws Exception {
        // given
        String mbti = "INFLUENCER";
        ResponseDto responseDto = new ResponseDto("member mbti change success");
        when(memberService.changeMemberMbti(mbti)).thenReturn(responseDto);

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/members/mbti")
                .with(csrf())
                .param("mbti", mbti)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}