package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.diary.Diary;
import com.hangangnow.mainserver.domain.diary.dto.DiaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataJpaDiaryRepository extends JpaRepository<Diary, Long> {

    @Query("select d from Diary d where d.member =: member and FUNCTION('month',d.diaryDate) =: month")
    List<Diary> findAllByMemberAndMonth(@Param("member") Member member,@Param("month") int month);

    default LocalDateTime update(Diary diary, DiaryDto diaryDto){
        diary.update(diaryDto);
        return diary.getLastModifiedTime();
    }

}
