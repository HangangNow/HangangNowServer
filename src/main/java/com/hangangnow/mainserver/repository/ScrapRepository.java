package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.event.Event;
import com.hangangnow.mainserver.domain.flyer.Flyer;
import com.hangangnow.mainserver.domain.picnic.RecomCourse;
import com.hangangnow.mainserver.domain.picnic.RecomPlace;
import com.hangangnow.mainserver.domain.scrap.*;
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


    public Optional<FlyerScrap> findFlyerScrapByMemberAndFlyer(Long flyerId, UUID memberId){

        List<FlyerScrap> resultList = em.createQuery("select fs from FlyerScrap fs" +
                        " where fs.flyer.id =:flyerId and fs.member.id =:memberId", FlyerScrap.class)
                .setParameter("flyerId", flyerId)
                .setParameter("memberId", memberId)
                .getResultList();

        return resultList.stream().findAny();
    }

    public Optional<RecomCourseScrap> findRecomCourseScrapByMemberAndEvent(Long recomCourseId, UUID memberId){

        List<RecomCourseScrap> resultList = em.createQuery("select rcs from RecomCourseScrap rcs" +
                        " where rcs.recomCourse.id =:recomCourseId and rcs.member.id =:memberId", RecomCourseScrap.class)
                .setParameter("recomCourseId", recomCourseId)
                .setParameter("memberId", memberId)
                .getResultList();

        return resultList.stream().findAny();
    }


    public Optional<RecomPlaceScrap> findRecomPlaceScrapByMemberAndEvent(Long recomPlaceId, UUID memberId){

        List<RecomPlaceScrap> resultList = em.createQuery("select rps from RecomPlaceScrap rps" +
                        " where rps.recomPlace.id =:recomPlaceId and rps.member.id =:memberId", RecomPlaceScrap.class)
                .setParameter("recomPlaceId", recomPlaceId)
                .setParameter("memberId", memberId)
                .getResultList();

        return resultList.stream().findAny();
    }


    public List<Event> findEventScrapsByMemberId(UUID memberId){
        return em.createQuery("select es.event from EventScrap es " +
                " join fetch es.event.photo" +
//                " join fetch es.event.thumbnailPhoto" +
                " where es.member.id =:memberId", Event.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }


    public List<Flyer> findFlyerScrapsByMemberId(UUID memberId){
        return em.createQuery("select fs.flyer From FlyerScrap fs" +
                " join fetch fs.flyer.photo" +
//                " join fetch fs.flyer.park" +
                " where fs.member.id =:memberId", Flyer.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }


    public List<RecomCourse> findRecomCourseByMemberId(UUID memberId){
        return em.createQuery("select rcs.recomCourse from RecomCourseScrap rcs" +
                " where rcs.member.id =:memberId", RecomCourse.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }


    public List<RecomPlace> findRecomPlaceByMemberId(UUID memberId){
        return em.createQuery("select rps.recomPlace from RecomPlaceScrap rps" +
                        " where rps.member.id =:memberId", RecomPlace.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }


    public void delete(Scrap scrap){
        em.remove(scrap);
    }
}
