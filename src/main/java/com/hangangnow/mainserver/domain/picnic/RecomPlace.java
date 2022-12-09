package com.hangangnow.mainserver.domain.picnic;

import com.hangangnow.mainserver.domain.common.Address;
import com.hangangnow.mainserver.domain.common.Local;
import com.hangangnow.mainserver.domain.scrap.RecomPlaceScrap;
import lombok.Getter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Entity
@Getter
public class RecomPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Address address;

    private Local local;

    @OneToMany(mappedBy = "recomPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecomPlaceScrap> recomPlaceScraps = new ArrayList<>();


    public Double getDistance(Double x_pos, Double y_pos){
        return sqrt(pow(this.local.getX_pos()-x_pos, 2) + pow(this.local.getY_pos()-y_pos, 2));
    }


    public void addRecomPlaceScrap(RecomPlaceScrap recomPlaceScrap){
        this.recomPlaceScraps.add(recomPlaceScrap);
    }


    public void deleteRecomPlaceScrap(RecomPlaceScrap recomPlaceScrap){
        this.recomPlaceScraps.remove(recomPlaceScrap);
    }

}
