package com.hangangnow.mainserver.sidefacility.api;

import com.hangangnow.mainserver.common.dto.GenericResponseDto;
import com.hangangnow.mainserver.config.security.SecurityConfig;
import com.hangangnow.mainserver.sidefacility.dto.FacilityResponseDto;
import com.hangangnow.mainserver.sidefacility.service.SideFacilityService;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SideFacilityController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class SideFacilityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SideFacilityService sideFacilityService;

    @WithMockUser
    @Test
    void 위도_경도와_일치하는_주변시설_조회() throws Exception {
        Double xpos = 37.1234;
        Double ypos = 126.5678;

        when(sideFacilityService.getSideFacilityByXposAndYpos(xpos, ypos))
                .thenReturn(getFacilityResponseDto());

        ResultActions resultActions = mockMvc.perform(get("/api/v1/facilities")
                .with(csrf())
                .param("x", String.valueOf(xpos))
                .param("y", String.valueOf(ypos))
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 한강_공원과_카테고리에_해당하는_주변시설_조회() throws Exception {
        Long parkId = 1L;
        String category = "CS2";

        when(sideFacilityService.getFacilitiesByParkIdAndType(parkId, category))
                .thenReturn(new GenericResponseDto(getFacilityResponseDto()));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/facilities/{parkId}", parkId)
                .with(csrf())
                .param("category", category)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    FacilityResponseDto getFacilityResponseDto() {
        return new FacilityResponseDto("서울시 영등포구 여의도동 87", "비비큐 여의도점", 37.1234, 126.5678);
    }
}