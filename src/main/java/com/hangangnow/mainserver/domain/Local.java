package com.hangangnow.mainserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Local {

    private String name;
    private Double x_pos;
    private Double y_pos;

    public Local(){
    }

    public Local(String name, Double x_pos, Double y_pos) {
        this.name = name;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
    }
}
