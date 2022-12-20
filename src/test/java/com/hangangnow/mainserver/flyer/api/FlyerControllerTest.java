package com.hangangnow.mainserver.flyer.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hangangnow.mainserver.config.security.SecurityConfig;
import com.hangangnow.mainserver.flyer.dto.FlyerRequestDto;
import com.hangangnow.mainserver.flyer.dto.FlyerResponseDto;
import com.hangangnow.mainserver.flyer.service.FlyerService;
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

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = FlyerController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
class FlyerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FlyerService flyerService;

    @MockBean
    DomainConverter domainConverter;

    ObjectMapper objectMapper = new ObjectMapper();

    @WithMockUser
    @Test
    void 단일_전단지_등록() throws Exception {
        // given
        MockMultipartFile multipartFile = createMultipartFile("files", "image.jpeg", "image/jpeg", "<<jpeg data>>");
        FlyerRequestDto flyerRequestDto = getFlyerRequestDto();
        when(flyerService.save(null, flyerRequestDto)).thenReturn(new FlyerResponseDto());

        // when
        ResultActions resultActions = mockMvc.perform(multipart("/api/v1/flyers")
                .file(multipartFile)
                .part(new MockPart("jsonData", objectMapper.writeValueAsBytes(flyerRequestDto)))
                .with(csrf())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
    }

    private MockMultipartFile createMultipartFile(String requestPart, String filename, String contentType, String originalContent) {
        return new MockMultipartFile(requestPart, filename, contentType, originalContent.getBytes());
    }

    @WithMockUser
    @Test
    void 단일_전단지_조회() throws Exception {
        // given
        FlyerResponseDto response = getFlyerResponseDto();
        doReturn(response).when(flyerService).findOneFlyers(1L);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/flyers/{flyerId}", "1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("name", response.getName()).exists())
                .andExpect(jsonPath("parkName", response.getParkName()).exists())
                .andExpect(jsonPath("call", response.getCall()).exists())
                .andExpect(jsonPath("address", response.getAddress()).exists());
    }


    FlyerRequestDto getFlyerRequestDto() {
        FlyerRequestDto flyerRequestDto = new FlyerRequestDto();
        flyerRequestDto.setName("여의도 비비큐");
        flyerRequestDto.setParkName("여의도한강공원");
        flyerRequestDto.setCall("02-781-1234");
        flyerRequestDto.setAddress("서울시 영등포구 국제금융로 7길");
        return flyerRequestDto;
    }

    FlyerResponseDto getFlyerResponseDto() {
        FlyerResponseDto flyerResponseDto = new FlyerResponseDto();
        flyerResponseDto.setName("여의도 비비큐");
        flyerResponseDto.setParkName("여의도한강공원");
        flyerResponseDto.setCall("02-781-1234");
        flyerResponseDto.setAddress("서울시 영등포구 국제금융로 7길");
        return flyerResponseDto;
    }
}