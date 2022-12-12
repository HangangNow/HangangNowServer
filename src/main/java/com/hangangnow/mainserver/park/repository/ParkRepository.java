package com.hangangnow.mainserver.park.repository;

import com.hangangnow.mainserver.park.entity.Park;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParkRepository {

    private final EntityManager em;

    public void save(Park park){
        em.persist(park);
    }

    public Optional<Park> findById(Long id){
        return Optional.ofNullable(em.find(Park.class, id));
    }


    public Optional<Park> findParkInfoById(Long id){
        List<Park> results = em.createQuery("select p from Park p" +
                        " join fetch p.photos pp" +
                        " where p.id =:id", Park.class)
                .setParameter("id", id)
                .getResultList();

        return results.stream().findAny();
    }

    public Optional<Park> findByName(String name){
        name = name.replace(" ", "");
        Park park = em.createQuery("select p from Park p where p.name =:name", Park.class)
                .setParameter("name", name)
                .getResultList()
                .get(0);
        return Optional.ofNullable(park);
    }

    public List<Park> findAll(){
        return em.createQuery("select p from Park p", Park.class).getResultList();
    }
}
