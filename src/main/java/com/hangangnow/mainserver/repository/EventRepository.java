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
        return em.createQuery("select distinct e from Event e" +
                        " join fetch e.photo ep" +
                        " join fetch e.thumbnailPhoto et", Event.class)
                .getResultList();
    }


    public Optional<Event> findById(Long id){
        return Optional.ofNullable(em.find(Event.class, id));
    }


    public void delete(Event event){
        em.remove(event);
    }
}
