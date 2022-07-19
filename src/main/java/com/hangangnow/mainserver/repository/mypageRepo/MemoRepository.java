package com.hangangnow.mainserver.repository.mypageRepo;

import com.hangangnow.mainserver.domain.member.Member;
import com.hangangnow.mainserver.domain.mypage.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemoRepository {

    private final EntityManager em;

    public void save(Memo memo){
        em.persist(memo);
    }

    public Optional<Memo> findById(Long id){
        return Optional.ofNullable(em.find(Memo.class, id));
    }

//    public List<Memo> findMemoByMember(Member member){
//        return em.createQuery("select m from Memo m where m.member =: member", Memo.class)
//                .setParameter("member", member)
//                .getResultList();
//    }

    public List<Memo> findAllByMemberAndMonth(Member member, int month){
        String jpql = "select m from Memo m where m.member =: member and FUNCTION('month',m.writtenDateTime) =: month";
        return em.createQuery(jpql, Memo.class)
                .setParameter("member", member)
                .setParameter("month", month)
                .getResultList();
    }

    public void update(Memo memoParam, String content){
        Memo findMemo = findById(memoParam.getId()).orElseThrow(() ->
                new NullPointerException("Fail update Memo: Memo not found"));
        findMemo.updateContent(content);
    }

    public void remove(Memo memoParam){
        Memo findMemo = findById(memoParam.getId()).orElseThrow(() ->
                new NullPointerException("Fail remove Memo: Memo not found"));
        em.remove(findMemo);
    }
}
