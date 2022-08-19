package com.hangangnow.mainserver.domain.picnic;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class RecomCourseWhere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recom_course_id")
    private RecomCourse RecomCourse;

    private int whereId;
}
