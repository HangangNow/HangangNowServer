package com.hangangnow.mainserver.domain.event.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class EventRequestDto {
    @NotEmpty
    private String address;

    private Double x_pos;
    private Double y_pos;

    @NotEmpty
    private String title;

    @NotEmpty
    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "날짜는 yyyy-mm-dd 타입입니다.")
    private String startDate;

    @NotEmpty
    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "날짜는 yyyy-mm-dd 타입입니다.")
    private String endDate;

    private String eventTime;

    private String price;

    @NotEmpty
    private String content;

    private String host;
    private String management;
}
