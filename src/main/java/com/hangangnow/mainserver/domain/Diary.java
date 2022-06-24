package com.hangangnow.mainserver.domain;

import com.hangangnow.mainserver.domain.photo.DiaryPhoto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Diary {
    @Id
    @GeneratedValue
    @Column(name = "diary_id")
    private Long id;
    //private User user;
    private String title;
    private String content;
    private Emotion emotion;
    private LocalDateTime writeDateTime;
    private LocalDateTime lastModifiedTime;
    private List<DiaryPhoto> photos = new ArrayList<DiaryPhoto>();


}
