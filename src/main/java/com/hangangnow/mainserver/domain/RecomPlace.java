package com.hangangnow.mainserver.domain;

import com.hangangnow.mainserver.domain.photo.RecomPlacePhoto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
public class RecomPlace {

    @Id
    @GeneratedValue
    @Column(name = "recom_place_id")
    private Long id;
    private String name;
    private Local local;
    private Address address;
    private String describe;
    private Mbti mbti;

    @OneToMany(mappedBy = "recomPlace")
    private List<RecomPlacePhoto> photos;

}
