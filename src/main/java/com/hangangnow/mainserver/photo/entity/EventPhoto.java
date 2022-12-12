package com.hangangnow.mainserver.photo.entity;

import com.hangangnow.mainserver.common.dto.S3UploadData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EventPhoto extends Photo{

    @Column(nullable = false)
    protected String s3Key;

    public EventPhoto(S3UploadData s3UploadData) {
        this.s3Key = s3UploadData.getFileKey();
        this.url = s3UploadData.getFileUrl();
        this.lastModifiedTime = LocalDateTime.now();
    }
}
