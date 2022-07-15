package com.hangangnow.mainserver.domain.photo;

import com.hangangnow.mainserver.domain.Park;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class ParkPhoto extends Photo {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "park_id")
    private Park park;
}
