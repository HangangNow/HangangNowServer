package com.hangangnow.mainserver.service.myPageService;

import com.hangangnow.mainserver.domain.mypage.Diary;
import com.hangangnow.mainserver.domain.photo.DiaryPhoto;
import com.hangangnow.mainserver.repository.mypageRepo.DiaryRepository;
import com.hangangnow.mainserver.repository.mypageRepo.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public void addDiaryPhoto(String url) throws NullPointerException{
        DiaryPhoto diaryPhoto = new DiaryPhoto(url);
        photoRepository.saveDairyPhoto(diaryPhoto);
    }
}
