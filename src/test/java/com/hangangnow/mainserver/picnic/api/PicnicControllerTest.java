package com.hangangnow.mainserver.picnic.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hangangnow.mainserver.common.entity.Address;
import com.hangangnow.mainserver.common.entity.Local;
import com.hangangnow.mainserver.config.security.SecurityConfig;
import com.hangangnow.mainserver.picnic.dto.RecomCourseDto;
import com.hangangnow.mainserver.picnic.dto.RecomCourseRequestDto;
import com.hangangnow.mainserver.picnic.dto.RecomCourseResponseDto;
import com.hangangnow.mainserver.picnic.dto.RecomPlaceResponseDto;
import com.hangangnow.mainserver.picnic.service.PicnicService;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PicnicController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
class PicnicControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PicnicService picnicService;

    ObjectMapper objectMapper = new ObjectMapper();

    @WithMockUser
    @Test
    void 단일_추천코스_조회() throws Exception {
        String courseId = "1";
        RecomCourseDto recomCourseDto = getRecomCourseDto();
        when(picnicService.getOneCourse(1L)).thenReturn(recomCourseDto);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/picnic/course/{courseId}", courseId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("name", recomCourseDto.getName()).exists())
                .andExpect(jsonPath("startPlaceName", recomCourseDto.getStartPlaceName()).exists())
                .andExpect(jsonPath("course", recomCourseDto.getCourse()).exists())
                .andExpect(jsonPath("length", recomCourseDto.getLength()).exists());
    }

    @WithMockUser
    @Test
    void 단일_추천장소_조회() throws Exception {
        String placeId = "1";
        RecomPlaceResponseDto recomPlaceDto = getRecomPlaceDto();
        when(picnicService.getOnePlace(1L)).thenReturn(recomPlaceDto);

        ResultActions resultActions = mockMvc.perform(get("/api/v1/picnic/place/{placeId}", placeId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("name", recomPlaceDto.getName()).exists())
                .andExpect(jsonPath("address", recomPlaceDto.getAddress().fullAddress()).exists());
    }

    @WithMockUser
    @Test
    void 위치기반_추천장소_조회() throws Exception {
        double x_pos = 127.3;
        double y_pos = 37.125;
        RecomPlaceResponseDto recomPlaceDto = getRecomPlaceDto();
        when(picnicService.getRecomPlaces(x_pos, y_pos, 1L)).thenReturn(List.of(recomPlaceDto));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/picnic/recom/place/")
                .param("x_pos", String.valueOf(x_pos))
                .param("y_pos", String.valueOf(y_pos))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 사용자_맞춤_추천코스_조회() throws Exception {
        double x_pos = 127.3;
        double y_pos = 37.125;
        String companion = "혼자";
        RecomCourseRequestDto recomCourseRequestDto = new RecomCourseRequestDto(x_pos, y_pos, companion);
        RecomCourseResponseDto recomCourseResponseDto = new RecomCourseResponseDto(true, List.of(), List.of());

        when(picnicService.getRecomCourses(recomCourseRequestDto)).thenReturn(recomCourseResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/picnic/recom/course")
                .with(csrf())
                .content(objectMapper.writeValueAsString(recomCourseRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8));

        resultActions.andExpect(status().isOk());
    }

    RecomCourseDto getRecomCourseDto() {
        return new RecomCourseDto(1L, "여의나루길", "9호선 국회의사당역", List.of("국회의사당", "여의도공원나들목", "물빛광장"), 3.5, true);
    }

    RecomPlaceResponseDto getRecomPlaceDto() {
        return new RecomPlaceResponseDto(1L, "서래나루", new Address("서울시 서초구 신반포로11길 40"), new Local("서래나루", 37.5, 127.3), true);
    }
}