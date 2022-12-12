package com.hangangnow.mainserver.scrap.entity;

import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.picnic.entity.RecomPlace;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class RecomPlaceScrap extends Scrap {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recom_place_id", nullable = false)
    private RecomPlace recomPlace;

    public void addMemberAndRecomPlace(Member member, RecomPlace recomPlace){
        this.member = member;
        member.addScarp(this);

        this.recomPlace = recomPlace;
        recomPlace.addRecomPlaceScrap(this);
    }


    public void cancelMemberAndRecomPlace(Member member, RecomPlace recomPlace){
        this.member = null;
        member.deleteScarp(this);

        this.recomPlace = null;
        recomPlace.deleteRecomPlaceScrap(this);
    }

}
