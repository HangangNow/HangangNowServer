package com.hangangnow.mainserver.diary.domain;

import com.hangangnow.mainserver.diary.dto.DiaryDto;
import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.photo.entity.DiaryPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private DiaryPhoto photo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate diaryDate;

    @Column(nullable = false)
    private LocalDateTime lastModifiedTime;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Enumerated(EnumType.STRING)
    private DiaryWeather diaryWeather;

    static public Diary of(DiaryDto diaryDto, Member member) {
        return Diary.builder()
                .member(member)
                .title(diaryDto.getTitle())
                .content(diaryDto.getContent())
                .diaryDate(LocalDate.parse(diaryDto.getDiaryDate(), DateTimeFormatter.ISO_DATE))
                .diaryWeather(FromStringToDiaryWeather(diaryDto.getDiaryWeather()))
                .emotion(FromStringToEmotion(diaryDto.getEmotion()))
                .lastModifiedTime(LocalDateTime.now())
                .build();
    }

    public void updateDiaryPhoto(DiaryPhoto diaryPhoto) {
        this.photo = diaryPhoto;
        if (diaryPhoto != null) diaryPhoto.updateDiary(this); // 굳이 필요하진 않지만 List라면 필요하다!
    }

    public void update(DiaryDto diaryDto) {
        this.title = diaryDto.getTitle();
        this.content = diaryDto.getContent();
        this.emotion = FromStringToEmotion(diaryDto.getEmotion());
        this.diaryWeather = FromStringToDiaryWeather(diaryDto.getDiaryWeather());
        this.lastModifiedTime = LocalDateTime.now();
    }

    static public Emotion FromStringToEmotion(String stringEmotion) {
        if (stringEmotion == null) return null;
        switch (stringEmotion) {
            case "EXCITED":
                return Emotion.EXCITED;
            case "BIG_SMILE":
                return Emotion.BIG_SMILE;
            case "FUNNY":
                return Emotion.FUNNY;
            case "PEACEFUL":
                return Emotion.PEACEFUL;
            case "LOVELY":
                return Emotion.LOVELY;
            case "SAD":
                return Emotion.SAD;
            case "ANGRY":
                return Emotion.ANGRY;
            case "FROWNING":
                return Emotion.FROWNING;
            case "DIZZY":
                return Emotion.DIZZY;
            default:
                return Emotion.SMILE;
        }
    }

    static public DiaryWeather FromStringToDiaryWeather(String stringDiaryWeather) {
        if (stringDiaryWeather == null) return null;
        switch (stringDiaryWeather) {
            case "SUN_CLOUD":
                return DiaryWeather.SUN_CLOUD;
            case "CLOUD":
                return DiaryWeather.CLOUD;
            case "CLOUD_RAIN":
                return DiaryWeather.CLOUD_RAIN;
            case "CLOUD_SNOW":
                return DiaryWeather.CLOUD_SNOW;
            case "SNOWMAN":
                return DiaryWeather.SNOWMAN;
            case "UMBRELLA":
                return DiaryWeather.UMBRELLA;
            case "WIND":
                return DiaryWeather.WIND;
            default:
                return DiaryWeather.SUN;
        }
    }
}
