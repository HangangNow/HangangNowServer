package com.hangangnow.mainserver.diary.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class DiaryDateRequestDto {

    @NotEmpty
    @Pattern(regexp = "^(20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "올바르지 않은 날짜 형식 입니다.")
    private String date;

}
