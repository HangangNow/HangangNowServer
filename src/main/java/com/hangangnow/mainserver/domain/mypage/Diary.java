package com.hangangnow.mainserver.domain.mypage;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.photo.DiaryPhoto;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime writeDateTime;

    @Column(nullable = false)
    private LocalDateTime lastModifiedTime;

    private Emotion emotion;

    private DiaryWeather diaryWeather;

    @OneToOne(mappedBy = "diary")
    private DiaryPhoto photo;

}
