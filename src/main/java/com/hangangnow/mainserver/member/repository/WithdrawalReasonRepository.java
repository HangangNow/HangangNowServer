package com.hangangnow.mainserver.member.repository;

import com.hangangnow.mainserver.member.entity.WithdrawalReason;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class WithdrawalReasonRepository {
    private final EntityManager em;

    public void save(WithdrawalReason withdrawalReason) {
        em.persist(withdrawalReason);
    }
}
