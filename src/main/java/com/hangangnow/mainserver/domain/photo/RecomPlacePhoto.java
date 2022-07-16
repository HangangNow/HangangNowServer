package com.hangangnow.mainserver.domain.photo;

import com.hangangnow.mainserver.domain.RecomPlace;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class RecomPlacePhoto extends Photo{

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recom_place_id")
    private RecomPlace recomPlace;
}
