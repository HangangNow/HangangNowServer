package com.hangangnow.mainserver.photo.entity;

import com.hangangnow.mainserver.common.dto.S3UploadData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class FlyerPhoto extends Photo {

    @Column(nullable = false)
    protected String s3Key;

    public FlyerPhoto(S3UploadData s3UploadData) {
        this.s3Key = s3UploadData.getFileKey();
        this.url = s3UploadData.getFileUrl();
        this.lastModifiedTime = LocalDateTime.now();
    }
}
