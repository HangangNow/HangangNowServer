package com.hangangnow.mainserver.domain.photo;

import com.hangangnow.mainserver.domain.S3UploadData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ThumbnailPhoto extends Photo{

    @Column(nullable = false)
    protected String s3Key;

    public ThumbnailPhoto(S3UploadData s3UploadData) {
        this.s3Key = s3UploadData.getFileKey();
        this.url = s3UploadData.getFileUrl();
        this.lastModifiedTime = LocalDateTime.now();
    }
}
