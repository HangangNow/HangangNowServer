package com.hangangnow.mainserver.picnic.repository;

import com.hangangnow.mainserver.picnic.entity.RecomCourse;
import com.hangangnow.mainserver.picnic.entity.RecomPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PicnicRepository {

    private final EntityManager em;

    public Optional<RecomCourse> findCourseById(Long id) {
        return Optional.ofNullable(em.find(RecomCourse.class, id));
    }

    public Optional<RecomPlace> findPlaceById(Long id) {
        return Optional.ofNullable(em.find(RecomPlace.class, id));
    }

    public List<RecomCourse> findAllCourseByPlace(List<String> places) {
        return em.createQuery("select r.RecomCourse from RecomCoursePlace r " +
                        "join fetch r.RecomCourse " +
                        "where r.place in :places", RecomCourse.class)
                .setParameter("places", places)
                .getResultList();
    }

    public List<RecomCourse> findAllCourseByTheme(List<String> themes) {
        return em.createQuery("select r.RecomCourse from RecomCourseTheme r " +
                        "join r.RecomCourse " +
                        "where r.theme in :themes", RecomCourse.class)
                .setParameter("themes", themes)
                .getResultList();
    }

    public List<RecomCourse> findAllCourseByPlaceAndTheme(List<String> places, List<String> themes) {
        return em.createQuery("select r1.RecomCourse from RecomCoursePlace r1, RecomCourseTheme r2 " +
                        "join r1.RecomCourse " +
                        "join r2.RecomCourse " +
                        "where r1.place in :places and " +
                        "r2.theme in :themes and " +
                        "r1.RecomCourse.id = r2.RecomCourse.id", RecomCourse.class)
                .setParameter("places", places)
                .setParameter("themes", themes)
                .getResultList();
    }

    public List<RecomPlace> findAllPlace() {
        return em.createQuery("select r from RecomPlace r").getResultList();
    }
}
