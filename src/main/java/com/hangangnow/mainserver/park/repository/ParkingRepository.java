package com.hangangnow.mainserver.park.repository;

import com.hangangnow.mainserver.park.entity.Parking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParkingRepository {
    private final EntityManager em;

    public Optional<Parking> findById(Long id){
        return Optional.ofNullable(em.find(Parking.class, id));
    }

    public List<Parking> findAll(){
        return em.createQuery("select p from Parking p", Parking.class)
                .getResultList();
    }
}
