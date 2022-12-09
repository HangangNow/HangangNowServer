package com.hangangnow.mainserver.config;

import com.hangangnow.mainserver.domain.event.dto.EventRequestDto;
import com.hangangnow.mainserver.domain.flyer.dto.FlyerRequestDto;
import com.hangangnow.mainserver.domain.diary.dto.DiaryDto;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.hangangnow.mainserver.config.BeanConfig.objectMapper;

public class DomainConverter {

    @Component
    public static class StringToDiaryDtoConvert implements Converter<String, DiaryDto> {
        @SneakyThrows
        @Override
        public DiaryDto convert(String value){
            return objectMapper().readValue(value, DiaryDto.class);
        }
    }

    @Component
    public static class StringToEventRequestDtoConvert implements Converter<String, EventRequestDto> {
        @SneakyThrows
        @Override
        public EventRequestDto convert(String value){
            return objectMapper().readValue(value, EventRequestDto.class);
        }
    }

    @Component
    public static class StringToFlyerRequestDtoConvert implements Converter<String, FlyerRequestDto> {
        @SneakyThrows
        @Override
        public FlyerRequestDto convert(String value){
            return objectMapper().readValue(value, FlyerRequestDto.class);
        }
    }

}
