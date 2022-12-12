package com.hangangnow.mainserver.photo.entity;

import com.hangangnow.mainserver.common.dto.S3UploadData;
import com.hangangnow.mainserver.diary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryPhoto extends Photo{

    @OneToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Column(nullable = false)
    protected String s3Key;

    public void update(String newUrl) {
        this.url = newUrl;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public DiaryPhoto(S3UploadData s3UploadData){
        this.s3Key = s3UploadData.getFileKey();
        this.url = s3UploadData.getFileUrl();
        this.lastModifiedTime = LocalDateTime.now();
    }

    public void updateDiary(Diary diary) {
        this.diary = diary;
    }

}
