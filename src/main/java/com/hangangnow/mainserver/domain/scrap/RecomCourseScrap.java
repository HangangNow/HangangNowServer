package com.hangangnow.mainserver.domain.scrap;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.picnic.RecomCourse;
import com.hangangnow.mainserver.domain.scrap.Scrap;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
public class RecomCourseScrap extends Scrap {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recom_course_id", nullable = false)
    private RecomCourse recomCourse;


    public void addMemberAndRecomCourse(Member member, RecomCourse recomCourse){
        this.member = member;
        member.addScarp(this);

        this.recomCourse = recomCourse;
        recomCourse.addRecomCourseScrap(this);
    }


    public void cancelMemberAndRecomCourse(Member member, RecomCourse recomCourse){
        this.member = null;
        member.deleteScarp(this);

        this.recomCourse = null;
        recomCourse.deleteRecomCourseScrap(this);
    }
}
