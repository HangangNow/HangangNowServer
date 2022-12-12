package com.hangangnow.mainserver.diary.repository;

import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.diary.domain.Diary;
import com.hangangnow.mainserver.diary.dto.DiaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    public List<Diary> findAllByMemberAndYearAndMonth(Member member, int year, int month) {
        String jpql = "select d from Diary d " +
                "where d.member =: member " +
                "and FUNCTION('year',d.diaryDate) =: year " +
                "and FUNCTION('month',d.diaryDate) =: month";

        return em.createQuery(jpql, Diary.class)
                .setParameter("member", member)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    public List<Diary> findAllByMemberAndDate(Member member, int year, int month, int day){
        String jpql = "select d from Diary d " +
                "where d.member =: member " +
                "and FUNCTION('year',d.diaryDate) =: year " +
                "and FUNCTION('month',d.diaryDate) =: month " +
                "and FUNCTION('day',d.diaryDate) =: day";
        return em.createQuery(jpql, Diary.class)
                .setParameter("member", member)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("day", day)
                .getResultList();
    }

    public LocalDateTime update(Diary diary, DiaryDto diaryDto){
        diary.update(diaryDto);
        return diary.getLastModifiedTime();
    }

    public void remove(Diary diary){
        em.remove(diary);
    }

}
