package com.hangangnow.mainserver.domain.picnic;

import com.hangangnow.mainserver.domain.common.Local;
import com.hangangnow.mainserver.domain.scrap.RecomCourseScrap;
import lombok.Getter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

@Entity
@Getter
public class RecomCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recom_course_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Embedded
    private Local startPlaceLocal;

    private String course;

    private Double length;

    @OneToMany(mappedBy = "recomCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecomCourseScrap> recomCourseScraps = new ArrayList<>();


    public Double getDistance(Double x_pos, Double y_pos){
        return sqrt(pow(this.startPlaceLocal.getX_pos()-x_pos, 2) + pow(this.startPlaceLocal.getY_pos()-y_pos, 2));
    }


    public void addRecomCourseScrap(RecomCourseScrap recomCourseScrap){
        this.recomCourseScraps.add(recomCourseScrap);
    }


    public void deleteRecomCourseScrap(RecomCourseScrap recomCourseScrap){
        this.recomCourseScraps.remove(recomCourseScrap);
    }
}
