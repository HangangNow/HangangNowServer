package com.hangangnow.mainserver.photo.entity;

import com.hangangnow.mainserver.common.dto.S3UploadData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPhoto extends Photo {

    @Column(nullable = false)
    protected String s3Key;

    public MemberPhoto(S3UploadData s3UploadData) {
        this.s3Key = s3UploadData.getFileKey();
        this.url = s3UploadData.getFileUrl();
        this.lastModifiedTime = LocalDateTime.now();
    }

}
