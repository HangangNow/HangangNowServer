package com.hangangnow.mainserver.picnic.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class RecomCourseTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recom_course_id")
    private RecomCourse RecomCourse;

    private String theme;
}
