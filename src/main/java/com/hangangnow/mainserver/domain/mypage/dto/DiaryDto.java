package com.hangangnow.mainserver.domain.mypage.dto;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.Diary;
import com.hangangnow.mainserver.domain.mypage.DiaryWeather;
import com.hangangnow.mainserver.domain.mypage.Emotion;
import com.hangangnow.mainserver.domain.mypage.Memo;
import com.hangangnow.mainserver.domain.photo.DiaryPhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiaryDto {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    @Pattern(regexp = "^(20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "올바르지 않은 날짜 형식 입니다.")
    private String diaryDate;

    private String emotion;

    private String diaryWeather;

    private String photo;

    public DiaryDto(Diary diary) {
        this.id = diary.getId();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.diaryDate = diary.getDiaryDate().toString();
        if(diary.getPhoto()!=null) this.photo = diary.getPhoto().getUrl();
        if(diary.getEmotion()!=null) this.emotion = diary.getEmotion().toString();
        if(diary.getDiaryWeather()!=null) this.diaryWeather = diary.getDiaryWeather().toString();
    }

}
