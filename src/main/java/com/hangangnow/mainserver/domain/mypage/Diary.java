package com.hangangnow.mainserver.domain.mypage;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDto;
import com.hangangnow.mainserver.domain.mypage.dto.MemoDto;
import com.hangangnow.mainserver.domain.photo.DiaryPhoto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Builder
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
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime lastModifiedDateTime;

    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Enumerated(EnumType.STRING)
    private DiaryWeather diaryWeather;

    @OneToOne(mappedBy = "diary")
    private DiaryPhoto photo;

    static public Diary of(DiaryDto diaryDto, Member member){
        return Diary.builder()
                .member(member)
                .title(diaryDto.getTitle())
                .content(diaryDto.getContent())
                .date(LocalDate.parse(diaryDto.getDate(), DateTimeFormatter.ISO_DATE))
                .diaryWeather(diaryDto.getDiaryWeather())
                .emotion(diaryDto.getEmotion())
                .lastModifiedDateTime(LocalDateTime.now())
                .photo(diaryDto.getPhoto())
                .build();
    }

    public void update(DiaryDto diaryDto){
        this.title = diaryDto.getTitle();
        this.content = diaryDto.getContent();
        this.emotion = diaryDto.getEmotion();
        this.diaryWeather = diaryDto.getDiaryWeather();
        this.photo = diaryDto.getPhoto(); // 연관관계 끊어주고 고아 방지를 위해 diary photo 삭제.
        this.lastModifiedDateTime = LocalDateTime.now();
    }

//    static public Emotion FromStringToEmotion(String stringEmotion){
//        switch (stringEmotion){
//            case "EXCITED" : return Emotion.EXCITED;
//            case "BIG_SMILE" : return Emotion.BIG_SMILE;
//            case "FUNNY" : return Emotion.FUNNY;
//            case "PEACEFUL" : return Emotion.PEACEFUL;
//            case "LOVELY" : return Emotion.LOVELY;
//            case "SAD" : return Emotion.SAD;
//            case "ANGRY" : return Emotion.ANGRY;
//            case "FROWNING" : return Emotion.FROWNING;
//            case "DIZZY" : return Emotion.DIZZY;
//            default: return Emotion.SMILE;
//        }
//    }

    static public MemoColor FromStringToMemoColor(String stringColor){
        switch (stringColor){
            case "RED" : return MemoColor.RED;
            case "ORANGE" : return MemoColor.ORANGE;
            case "YELLOW" : return MemoColor.YELLOW;
            case "GREEN" : return MemoColor.GREEN;
            case "BLUE" : return MemoColor.BLUE;
            case "NAVY" : return MemoColor.NAVY;
            case "PURPLE" : return MemoColor.PURPLE;
            default: return MemoColor.GRAY;
        }
    }
}
