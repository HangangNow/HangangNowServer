package com.hangangnow.mainserver.domain.scrap;

import com.hangangnow.mainserver.domain.common.Local;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class LocalScrap extends Scrap{

    @Column(nullable = false)
    private Local local;

}
