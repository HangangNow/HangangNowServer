package com.hangangnow.mainserver.repository.mypageRepo;

import com.hangangnow.mainserver.domain.mypage.WithdrawalReason;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class WithdrawalReasonRepository {
    private final EntityManager em;

    public void save(WithdrawalReason withdrawalReason){
        em.persist(withdrawalReason);
    }
}
