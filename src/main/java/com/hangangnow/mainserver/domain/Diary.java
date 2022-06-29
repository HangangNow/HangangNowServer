package com.hangangnow.mainserver.domain;

import com.hangangnow.mainserver.domain.photo.DiaryPhoto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @OneToOne(mappedBy = "diary")
    private DiaryPhoto photo;

}
