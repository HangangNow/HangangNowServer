package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.photo.DiaryPhoto;
import com.hangangnow.mainserver.domain.photo.MemberPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {

    private final EntityManager em;

    public void saveDairyPhoto(DiaryPhoto diaryPhoto){
        em.persist(diaryPhoto);
    }

    public void removeDairyPhoto(DiaryPhoto diaryPhoto){
        em.remove(diaryPhoto);
    }

    public void removeMemberPhoto(MemberPhoto memberPhoto){
        em.remove(memberPhoto);
    }

//    public LocalDateTime update(DiaryPhoto diaryPhoto, String newUrl){
//        diaryPhoto.update(newUrl);
//    }


}
