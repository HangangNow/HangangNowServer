package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public Member save(Member member){
        em.persist(member);
        return member;
    }

    
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id){
        List<Member> members = em.createQuery("select m from Member m where m.id =:id", Member.class)
                .setParameter("id", id)
                .getResultList();
        return members.stream().findAny();
    }

    public Optional<Member> findByLoginId(String loginId){
        List<Member> members = em.createQuery("select m from Member m where m.loginId =:loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return members.stream().findAny();
    }


    public Optional<Member> findByEmail(String email){
        List<Member> members = em.createQuery("select m from Member m where m.email =:email", Member.class)
                .setParameter("email", email)
                .getResultList();
        return members.stream().findAny();
    }





}
