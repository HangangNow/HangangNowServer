package com.hangangnow.mainserver.domain.mypage.scrap;

import com.hangangnow.mainserver.domain.Local;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class LocalScrap extends Scrap{

    @Column(nullable = false)
    private Local local;

}
