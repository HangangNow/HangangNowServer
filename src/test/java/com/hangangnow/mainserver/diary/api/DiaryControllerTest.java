package com.hangangnow.mainserver.diary.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hangangnow.mainserver.config.security.SecurityConfig;
import com.hangangnow.mainserver.diary.dto.DiaryDto;
import com.hangangnow.mainserver.diary.service.DiaryService;
import com.hangangnow.mainserver.util.DomainConverter;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DiaryController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class DiaryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DomainConverter domainConverter;

    @MockBean
    DiaryService diaryService;

    ObjectMapper objectMapper = new ObjectMapper();

    @WithMockUser
    @Test
    void 단일_일기_생성() throws Exception {
        MockMultipartFile multipartFile = createMultipartFile("files", "image.jpeg", "image/jpeg", "<<jpeg data>>");
        DiaryDto diaryDto = getDiaryDto();
        when(diaryService.addDiary(diaryDto, multipartFile)).thenReturn(diaryDto);

        ResultActions resultActions = mockMvc.perform(multipart("/api/v1/diary")
                .file(multipartFile)
                .part(new MockPart("jsonData", objectMapper.writeValueAsBytes(diaryDto)))
                .with(csrf())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 단일_일기_조회() throws Exception {
        String diaryId = "1";
        DiaryDto diaryDto = getDiaryDto();
        when(diaryService.findOne(1L)).thenReturn(diaryDto);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/diary/{diaryId}", diaryId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("title", "일기 제목").exists())
                .andExpect(jsonPath("content", "일기 본문").exists())
                .andExpect(jsonPath("diaryDate", "2022-12-25").exists())
                .andExpect(jsonPath("emotion", "LOVELY").exists())
                .andExpect(jsonPath("diaryWeather", "SUN").exists());
    }

    @WithMockUser
    @Test
    void 단일_일기_삭제() throws Exception {
        String diaryId = "1";
        when(diaryService.deleteDiary(1L)).thenReturn(true);

        ResultActions resultActions = mockMvc.perform(delete("/api/v1/diary/{diaryId}", diaryId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 단일_일기_수정() throws Exception {
        String diaryId = "1";
        DiaryDto diaryDto = getDiaryDto();
        MockMultipartFile multipartFile = createMultipartFile("files", "image.jpeg", "image/jpeg", "<<jpeg data>>");
        when(diaryService.modifyDiary(1L, diaryDto, multipartFile)).thenReturn(true);

        ResultActions resultActions = mockMvc.perform(multipart("/api/v1/diary/{diaryId}", diaryId)
                .file(multipartFile)
                .part(new MockPart("jsonData", objectMapper.writeValueAsBytes(diaryDto)))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }));

        resultActions.andExpect(status().isOk());
    }

    private MockMultipartFile createMultipartFile(String requestPart, String filename, String contentType, String originalContent) {
        return new MockMultipartFile(requestPart, filename, contentType, originalContent.getBytes());
    }

    DiaryDto getDiaryDto() {
        return new DiaryDto(1L, "일기 제목", "일기 본문", "2022-12-25", "LOVELY", "SUN", "testURL");
    }
}