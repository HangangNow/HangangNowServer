package com.hangangnow.mainserver.member.repository;

import com.hangangnow.mainserver.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }


    public void delete(Member member) {
        em.remove(member);
    }


    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(UUID id) {
        List<Member> members = em.createQuery("select m from Member m where m.id =:id", Member.class)
                .setParameter("id", id)
                .getResultList();
        return members.stream().findAny();
    }

    public Optional<Member> findByLoginId(String loginId) {
        List<Member> members = em.createQuery("select m from Member m where m.loginId =:loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return members.stream().findAny();
    }


    public Optional<Member> findByEmail(String email) {
        List<Member> members = em.createQuery("select m from Member m where m.email =:email", Member.class)
                .setParameter("email", email)
                .getResultList();
        return members.stream().findAny();
    }

    public Optional<Member> findByIdWithScrap(UUID id) {
        List<Member> members = em.createQuery("select m from Member m" +
                        " join fetch m.scraps" +
                        " where m.id =:id", Member.class)
                .setParameter("id", id)
                .getResultList();
        return members.stream().findAny();
    }
}
