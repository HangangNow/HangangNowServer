package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.park.Park;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkRepository {

    private final EntityManager em;

    public void save(Park park){
        em.persist(park);
    }

    public Park findById(Long id){
        return em.find(Park.class, id);
    }

    public Park findByName(String name){
        return em.createQuery("select p from Park p where p.name =:name", Park.class)
                .setParameter("name", name)
                .getResultList()
                .get(0);
    }

    public List<Park> findAll(){
        return em.createQuery("select p from Park p", Park.class).getResultList();
    }
}
