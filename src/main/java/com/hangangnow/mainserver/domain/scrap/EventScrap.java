package com.hangangnow.mainserver.domain.scrap;

import com.hangangnow.mainserver.domain.event.Event;
import com.hangangnow.mainserver.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class EventScrap extends Scrap{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public void addMemberAndEvent(Member member, Event event){
        this.member = member;
        member.addScarp(this);

        this.event = event;
        event.addEventScrap(this);
    }


    public void cancelMemberAndEvent(Member member, Event event){
        this.member = null;
        member.deleteScarp(this);

        this.event = null;
        event.deleteEventScrap(this);
    }

}
