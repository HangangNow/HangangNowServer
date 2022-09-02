package com.hangangnow.mainserver.domain.mypage.dto;


import com.hangangnow.mainserver.domain.picnic.RecomCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomCourseScrapDto {

    private Long id;
    private String name;
    private String startPlaceName;
    private Double x_pos;
    private Double y_pos;
    private String course;
    private Double length;

    public RecomCourseScrapDto(RecomCourse recomCourse) {
        this.id = recomCourse.getId();
        this.name = recomCourse.getName();
        this.startPlaceName = recomCourse.getStartPlaceLocal().getLocalname();
        this.x_pos = recomCourse.getStartPlaceLocal().getX_pos();
        this.y_pos = recomCourse.getStartPlaceLocal().getY_pos();
        this.course = recomCourse.getCourse();
        this.length = recomCourse.getLength();
    }
}
