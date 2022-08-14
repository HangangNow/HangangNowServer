package com.hangangnow.mainserver.repository;

import com.hangangnow.mainserver.domain.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRepository {
    private final EntityManager em;

    public void save(Event event){
        em.persist(event);
    }


    public List<Event> findAllEvents(){
        return em.createQuery("select e from Event e" +
                        " join fetch e.photo ep" +
                        " join fetch e.thumbnailPhoto et", Event.class)
                .getResultList();
    }


    public Optional<Event> findById(Long id){
        Event event = em.createQuery("select e from Event e" +
                        " join fetch e.photo ep" +
                        " join fetch e.thumbnailPhoto et" +
                        " where e.id =:id", Event.class)
                .setParameter("id", id)
                .getSingleResult();

        return Optional.ofNullable(event);
    }


    public Optional<Event> findByIdToScrap(Long id){
        Event event = em.createQuery("select e from Event e" +
                        " join fetch e.eventScraps es" +
                        " where e.id =:id", Event.class)
                .setParameter("id", id)
                .getSingleResult();

        return Optional.ofNullable(event);
    }



    public void delete(Event event){
        em.remove(event);
    }
}
