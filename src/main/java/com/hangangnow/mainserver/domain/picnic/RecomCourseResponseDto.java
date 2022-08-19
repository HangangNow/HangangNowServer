package com.hangangnow.mainserver.domain.picnic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecomCourseResponseDto {

    private Boolean matchingSuccess;
    private List<RecomCourseDto> courses;
    private List<RecomPlace> places;
}
