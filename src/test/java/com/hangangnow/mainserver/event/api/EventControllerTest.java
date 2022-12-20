package com.hangangnow.mainserver.event.api;

import com.hangangnow.mainserver.config.security.SecurityConfig;
import com.hangangnow.mainserver.event.service.EventService;
import com.hangangnow.mainserver.util.DomainConverter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DomainConverter domainConverter;

    @MockBean
    EventService eventService;

    @WithMockUser
    @Test
    void 모든_이벤트_조회() throws Exception {
        // given
        doReturn(null).when(eventService).findAllEvents();

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 단일_이벤트_조회() throws Exception {
        // given
        String eventId = "1";
        doReturn(null).when(eventService).findOne(1L);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/events/{eventId}", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void 단일_이벤트_삭제() throws Exception {
        // given
        String eventId = "1";
        doReturn(null).when(eventService).delete(1L);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/events/{eventId}", eventId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }
}