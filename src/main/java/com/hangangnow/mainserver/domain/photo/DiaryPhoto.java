package com.hangangnow.mainserver.domain.photo;

import com.hangangnow.mainserver.domain.Diary;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
public class DiaryPhoto extends Photo{

    @OneToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;
}
