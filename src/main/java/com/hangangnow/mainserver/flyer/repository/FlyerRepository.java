package com.hangangnow.mainserver.flyer.repository;

import com.hangangnow.mainserver.flyer.entity.Flyer;
import com.hangangnow.mainserver.park.entity.Park;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FlyerRepository {
    private final EntityManager em;

    public Flyer save(Flyer flyer) {
        em.persist(flyer);
        return flyer;
    }

    public Optional<Flyer> findById(Long id) {
        return Optional.ofNullable(em.find(Flyer.class, id));
    }

    public List<Flyer> findAllFlyerByPark(Park park) {
        return em.createQuery("select f from Flyer f " +
                        " join fetch f.park fp" +
                        " join fetch f.photo fph" +
                        " where f.park =:park", Flyer.class)
                .setParameter("park", park)
                .getResultList();
    }

    public List<Flyer> findAllFlyer() {
        return em.createQuery("select f from Flyer f" +
                        " join fetch f.photo ph" +
                        " join fetch f.park p", Flyer.class)
                .getResultList();
    }

    public void delete(Flyer flyer) {
        em.remove(flyer);
    }
}
