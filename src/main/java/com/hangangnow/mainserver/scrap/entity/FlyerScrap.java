package com.hangangnow.mainserver.scrap.entity;

import com.hangangnow.mainserver.flyer.entity.Flyer;
import com.hangangnow.mainserver.member.entity.Member;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
public class FlyerScrap extends Scrap{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flyer_id", nullable = false)
    private Flyer flyer;

    public void addMemberAndEvent(Member member, Flyer flyer){
        this.member = member;
        member.addScarp(this);

        this.flyer = flyer;
        flyer.addFlyerScrap(this);
    }

    public void cancelMemberAndEvent(Member member, Flyer flyer){
        this.member = null;
        member.deleteScarp(this);

        this.flyer = null;
        flyer.deleteFlyerScrap(this);
    }
}
