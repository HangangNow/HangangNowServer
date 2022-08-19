package com.hangangnow.mainserver.domain.mypage.scrap;

import com.hangangnow.mainserver.domain.picnic.RecomPlace;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class RecomPlaceScrap extends Scrap{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recom_place_id", nullable = false)
    private RecomPlace recomPlace;

}
