package com.hangangnow.mainserver.domain.picnic;

import com.hangangnow.mainserver.domain.Local;
import lombok.Getter;

import javax.persistence.*;

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

    public Double getDistance(Double x_pos, Double y_pos){
        return sqrt(pow(this.startPlaceLocal.getX_pos()-x_pos, 2) + pow(this.startPlaceLocal.getY_pos()-y_pos, 2));
    }
}
