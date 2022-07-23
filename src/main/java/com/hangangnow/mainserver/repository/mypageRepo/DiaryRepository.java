package com.hangangnow.mainserver.repository.mypageRepo;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.Diary;
import com.hangangnow.mainserver.domain.mypage.Memo;
import com.hangangnow.mainserver.domain.mypage.dto.DiaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    public List<Diary> findByMember(Member member){
        return em.createQuery("select d from Diary d where d.member =: member", Diary.class)
                .setParameter("member", member)
                .getResultList();
    }

    public List<Diary> findAllByMemberAndMonth(Member member, int month){
        String jpql = "select d from Diary d where d.member =: member and FUNCTION('month',d.date) =: month";
        return em.createQuery(jpql, Diary.class)
                .setParameter("member", member)
                .setParameter("month", month)
                .getResultList();
    }

    public List<Diary> findAllByMemberAndYearAndMonth(Member member, int year, int month) {
        String jpql = "select d from Diary d " +
                "where d.member =: member " +
                "and FUNCTION('year',d.date) =: year " +
                "and FUNCTION('month',d.date) =: month";

        return em.createQuery(jpql, Diary.class)
                .setParameter("member", member)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    public List<Diary> findAllByMemberAndWrittenDateTime(Member member, int year, int month, int day){
        String jpql = "select d from Diary d " +
                "where d.member =: member " +
                "and FUNCTION('year',d.date) =: year " +
                "and FUNCTION('month',d.date) =: month " +
                "and FUNCTION('day',d.date) =: day";
        return em.createQuery(jpql, Diary.class)
                .setParameter("member", member)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("day", day)
                .getResultList();
    }

    public LocalDateTime update(Diary diary, DiaryDto diaryDto){
        diary.update(diaryDto);
        return diary.getLastModifiedDateTime();
    }

    public void remove(Diary diary){
        em.remove(diary);
    }

}
