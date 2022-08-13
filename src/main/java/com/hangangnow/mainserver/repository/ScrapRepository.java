package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.event.Event;
import com.hangangnow.mainserver.domain.mypage.scrap.EventScrap;
import com.hangangnow.mainserver.domain.mypage.scrap.Scrap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ScrapRepository {
    private final EntityManager em;

    public Optional<EventScrap> findEventScrapByMemberAndEvent(Long eventId, UUID memberId){

        List<EventScrap> resultList = em.createQuery("select es from EventScrap es" +
                        " where es.event.id =:eventId and es.member.id =:memberId", EventScrap.class)
                .setParameter("eventId", eventId)
                .setParameter("memberId", memberId)
                .getResultList();

        return resultList.stream().findAny();
    }


    public void delete(Scrap scrap){
        em.remove(scrap);
    }
}
