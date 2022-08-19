package com.hangangnow.mainserver.domain.picnic;

import com.hangangnow.mainserver.domain.Address;
import com.hangangnow.mainserver.domain.Local;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public Double getDistance(Double x_pos, Double y_pos){
        return sqrt(pow(this.local.getX_pos()-x_pos, 2) + pow(this.local.getY_pos()-y_pos, 2));
    }
}
