package com.hangangnow.mainserver.domain.mypage.scrap;

import com.hangangnow.mainserver.domain.Event;
import com.hangangnow.mainserver.domain.Local;
import com.hangangnow.mainserver.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class EventScrap extends Scrap{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

}
