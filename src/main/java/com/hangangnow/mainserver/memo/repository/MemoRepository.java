package com.hangangnow.mainserver.memo.repository;

import com.hangangnow.mainserver.member.entity.Member;
import com.hangangnow.mainserver.memo.entity.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemoRepository {

    private final EntityManager em;

    public void save(Memo memo){ em.persist(memo); }

    public Optional<Memo> findById(Long id){
        return Optional.ofNullable(em.find(Memo.class, id));
    }

//    public List<Memo> findAllByMember(Member member){
//        return em.createQuery("select m from Memo m where m.member =: member", Memo.class)
//                .setParameter("member", member)
//                .getResultList();
//    }

    public List<Memo> findAllByMemberAndYearAndMonth(Member member,int year, int month){
        String jpql = "select m from Memo m where m.member =: member " +
                "and FUNCTION('year',m.memoDate) =: year " +
                "and FUNCTION('month',m.memoDate) =: month ";

        return em.createQuery(jpql, Memo.class)
                .setParameter("member", member)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    public LocalDateTime update(Memo memo, String content, String memoColor){
        memo.update(content, memoColor);
        return memo.getLastModifiedTime();
    }

    public void remove(Memo memo){
        em.remove(memo);
    }
}

