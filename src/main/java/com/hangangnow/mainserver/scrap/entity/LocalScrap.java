package com.hangangnow.mainserver.scrap.entity;

import com.hangangnow.mainserver.common.entity.Local;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class LocalScrap extends Scrap {

    @Column(nullable = false)
    private Local local;

}
