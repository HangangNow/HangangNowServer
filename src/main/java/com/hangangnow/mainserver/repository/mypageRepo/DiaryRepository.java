package com.hangangnow.mainserver.repository.mypageRepo;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DiaryRepository {

    private final EntityManager em;

    public void save(Diary diary){
        em.persist(diary);
    }

    public Optional<Diary> findById(Long id){
        return Optional.ofNullable(em.find(Diary.class, id));
    }

    public List<Diary> findAllByMemberAndMonth(Member member, int month){
        String jpql = "select d from Diary d where d.member =: member and FUNCTION('month',d.writtenDateTime) =: month";
        return em.createQuery(jpql, Diary.class)
                .setParameter("member", member)
                .setParameter("month", month)
                .getResultList();
    }

    public void update(Diary diaryParam){
        Diary findDiary = findById(diaryParam.getId()).orElseThrow(() ->
                new NullPointerException("Fail remove diary: Diary not found"));
        findDiary.update(diaryParam);
    }

    public void remove(Diary diaryParam){
        Diary findDiary = findById(diaryParam.getId()).orElseThrow(() ->
                new NullPointerException("Fail remove diary: Diary not found"));
        em.remove(findDiary);
    }

}
