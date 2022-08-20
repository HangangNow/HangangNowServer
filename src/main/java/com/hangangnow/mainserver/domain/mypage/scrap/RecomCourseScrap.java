package com.hangangnow.mainserver.domain.mypage.scrap;

import com.hangangnow.mainserver.domain.picnic.RecomCourse;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
public class RecomCourseScrap extends Scrap{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recom_course_id", nullable = false)
    private RecomCourse recomCourse;
}
