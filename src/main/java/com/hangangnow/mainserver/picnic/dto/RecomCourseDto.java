package com.hangangnow.mainserver.picnic.dto;

import com.hangangnow.mainserver.picnic.entity.RecomCourse;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
public class RecomCourseDto {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String startPlaceName;

    @NotNull
    private List<String> course = new ArrayList<>();

    @NotNull
    private Double length;

    private Boolean isScrap;

    public RecomCourseDto(RecomCourse recomCourse){
        this.id = recomCourse.getId();
        this.name = recomCourse.getName();
        this.startPlaceName = recomCourse.getStartPlaceLocal().getLocalname();
        this.length = recomCourse.getLength();
        this.course = Arrays.stream(recomCourse.getCourse().split("-")).collect(Collectors.toList());
    }

    public void setIsScrap(Boolean isScrap){
        this.isScrap = isScrap;
    }
}
