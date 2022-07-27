package com.hangangnow.mainserver.domain.mypage.dto;

import com.hangangnow.mainserver.domain.mypage.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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

    private String url;

    public DiaryDto(Diary diary) {
        this.id = diary.getId();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.diaryDate = diary.getDiaryDate().toString();
        if(diary.getPhoto()!=null) this.url = diary.getPhoto().getUrl();
        if(diary.getEmotion()!=null) this.emotion = diary.getEmotion().toString();
        if(diary.getDiaryWeather()!=null) this.diaryWeather = diary.getDiaryWeather().toString();
    }

}
