package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.member.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor

public class RefreshTokenRepository {

    private final EntityManager em;

    public RefreshToken save(RefreshToken refreshToken){
        em.persist(refreshToken);
        return refreshToken;
    }


    public void delete(UUID key) {
        em.createQuery("delete from RefreshToken rt where rt.key =:key")
                .setParameter("key", key)
                .executeUpdate();
    }


    public Optional<RefreshToken> findByKey(UUID key){
        List<RefreshToken> refreshTokens = em.createQuery("select r from RefreshToken r where r.key =:key", RefreshToken.class)
                .setParameter("key", key)
                .getResultList();

        return refreshTokens.stream().findAny();
    }
}
