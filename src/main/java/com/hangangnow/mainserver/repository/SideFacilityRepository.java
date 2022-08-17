package com.hangangnow.mainserver.repository;


import com.hangangnow.mainserver.domain.sidefacility.FacilityType;
import com.hangangnow.mainserver.domain.sidefacility.SideFacility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SideFacilityRepository {

    private final EntityManager em;

    public void save(SideFacility sideFacility){
        em.persist(sideFacility);
    }

    public List<SideFacility> findByParkAndType(Long parkId, FacilityType facilityType){
        return em.createQuery("select sf from SideFacility sf" +
                " where sf.facilityType =:facilityType and sf.park.id =:parkId", SideFacility.class)
                .setParameter("parkId", parkId)
                .setParameter("facilityType", facilityType)
                .getResultList();
    }

}
