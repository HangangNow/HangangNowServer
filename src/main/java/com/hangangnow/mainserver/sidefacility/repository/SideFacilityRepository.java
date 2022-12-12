package com.hangangnow.mainserver.sidefacility.repository;


import com.hangangnow.mainserver.sidefacility.entity.FacilityType;
import com.hangangnow.mainserver.sidefacility.entity.SideFacility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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


    public Optional<SideFacility> findByFacilityWithXY(Double x_pos, Double y_pos){
        List<SideFacility> resultList = em.createQuery("select sf from SideFacility sf" +
                        " where sf.local.x_pos =:x_pos and sf.local.y_pos =:y_pos", SideFacility.class)
                .setParameter("x_pos", x_pos)
                .setParameter("y_pos", y_pos)
                .getResultList();

        return resultList.stream().findAny();
    }

}
