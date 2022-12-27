package com.hangangnow.mainserver.memo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hangangnow.mainserver.config.security.SecurityConfig;
import com.hangangnow.mainserver.memo.dto.MemoDto;
import com.hangangnow.mainserver.memo.service.MemoService;
import com.hangangnow.mainserver.util.DomainConverter;
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


import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemoController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
class MemoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemoService memoService;

    @MockBean
    DomainConverter domainConverter;

    ObjectMapper objectMapper = new ObjectMapper();

    @WithMockUser
    @Test
    void 단일_메모_조회() throws Exception {
        String memoId = "1";
        MemoDto memoDto = getMemoDto();
        when(memoService.findOne(1L)).thenReturn(memoDto);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/memos/{memoId}", memoId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("content", memoDto.getContent()).exists())
                .andExpect(jsonPath("color", memoDto.getColor()).exists())
                .andExpect(jsonPath("memoDate", memoDto.getMemoDate()).exists());
    }

    @WithMockUser
    @Test
    void 단일_메모_등록() throws Exception {
        MemoDto memoDto = getMemoDto();
        when(memoService.addMemo(memoDto)).thenReturn(memoDto);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/memos")
                .with(csrf())
                .content(objectMapper.writeValueAsString(memoDto))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 단일_메모_수정() throws Exception {
        String memoId = "1";
        MemoDto memoDto = getMemoDto();
        when(memoService.modifyMemo(1L, memoDto)).thenReturn(true);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/memos/{memoId}", memoId)
                .with(csrf())
                .content(objectMapper.writeValueAsString(memoDto))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 단일_메모_삭제() throws Exception {
        String memoId = "1";
        when(memoService.deleteMemo(1L)).thenReturn(true);

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/memos/{memoId}", memoId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    MemoDto getMemoDto() {
        return new MemoDto(1L, "메모 테스트", "#ffffff", "2022-12-27");
    }
}