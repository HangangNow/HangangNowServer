package com.hangangnow.mainserver.domain.photo;

import com.hangangnow.mainserver.domain.S3UploadData;
import com.hangangnow.mainserver.domain.event.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EventPhoto extends Photo{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(nullable = false)
    protected String s3Key;

    public EventPhoto(S3UploadData s3UploadData) {
        this.s3Key = s3UploadData.getFileKey();
        this.url = s3UploadData.getFileUrl();
        this.lastModifiedTime = LocalDateTime.now();
    }
}
