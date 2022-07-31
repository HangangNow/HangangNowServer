package com.hangangnow.mainserver.domain.photo;

import com.hangangnow.mainserver.domain.S3UploadData;
import com.hangangnow.mainserver.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

//@Entity
//@Getter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
public class MemberPhoto extends Photo{

//    @OneToOne
//    @JoinColumn(name = "member_id")
//    private Member member;

//    public MemberPhoto(S3UploadData s3UploadData) {
//        this.s3Key = s3UploadData.getFileKey();
//        this.url = s3UploadData.getFileUrl();
//        this.lastModifiedTime = LocalDateTime.now();
//
//    }
//
//    public void update(String newUrl) {
//        this.url = newUrl;
//        this.lastModifiedTime = LocalDateTime.now();
//    }
}
